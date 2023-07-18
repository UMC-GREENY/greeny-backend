package greeny.backend.domain.member.service;

import greeny.backend.config.jwt.JwtProvider;
import greeny.backend.domain.member.dto.sign.LoginRequestDto;
import greeny.backend.domain.member.dto.sign.SignUpRequestDto;
import greeny.backend.domain.member.dto.sign.TokenDto;
import greeny.backend.domain.member.dto.sign.TokenResponseDto;
import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.member.repository.*;
import greeny.backend.exception.situation.EmailAlreadyExistsException;
import greeny.backend.exception.situation.MemberGeneralNotFoundException;
import greeny.backend.exception.situation.MemberNotFoundException;
import greeny.backend.exception.situation.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberAgreementRepository memberAgreementRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public void validateSignUpInfo(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfo(signUpRequestDto.getEmail());
        saveGeneralMember(signUpRequestDto);
    }

    @Transactional
    public TokenResponseDto signInWithGeneral(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.getEmail();
        Member foundMember = getMember(email);

        MemberGeneral foundMemberGeneral = getMemberGeneral(foundMember);
        String foundIsAuto = foundMemberGeneral.getIsAuto();

        changeIsAutoBySignIn(loginRequestDto.getIsAuto(), foundMemberGeneral, foundIsAuto);

        return authorize(email, foundMember.getId().toString());
    }

    private Member toMember(String email) {
        return Member.builder()
                .email(email)
                .role(Role.ROLE_USER)
                .build();
    }

    private MemberGeneral toMemberGeneral(Member member, String password) {
        return MemberGeneral.builder()
                .member(member)
                .password(passwordEncoder.encode(password))
                .isAuto("0")
                .build();
    }

    private MemberSocial toMemberSocial(Member member, Provider provider) {
        return MemberSocial.builder()
                .member(member)
                .provider(provider)
                .build();
    }

    private MemberProfile toMemberProfile(Member member, String name, String phone, String birth) {
        return MemberProfile.builder()
                .member(member)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
    }

    private MemberAgreement toMemberAgreement(Member member, String personalInfo, String thirdParty) {
        return MemberAgreement.builder()
                .member(member)
                .personalInfo(personalInfo)
                .thirdParty(thirdParty)
                .build();
    }

    private void saveGeneralMember(SignUpRequestDto signUpRequestDto) {
        Member savedMember = memberRepository.save(toMember(signUpRequestDto.getEmail()));
        memberGeneralRepository.save(toMemberGeneral(savedMember, signUpRequestDto.getPassword()));
        memberProfileRepository.save(toMemberProfile(savedMember, signUpRequestDto.getName(), signUpRequestDto.getPhone(), signUpRequestDto.getBirth()));
        memberAgreementRepository.save(toMemberAgreement(savedMember, signUpRequestDto.getPersonalInfo(), signUpRequestDto.getThirdParty()));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private MemberGeneral getMemberGeneral(Member member) {
        return memberGeneralRepository.findByMember(member)
                .orElseThrow(MemberGeneralNotFoundException::new);
    }

    private void changeIsAutoBySignIn(String isAutoToCheck, MemberGeneral foundMemberGeneral, String foundIsAuto) {
        if (isAutoToCheck.equals("0")) {
            if (foundIsAuto.equals("1")) {
                foundMemberGeneral.changeIsAuto("0");
            }
        } else {
            if (foundIsAuto.equals("0")) {
                foundMemberGeneral.changeIsAuto("1");
            }
        }
    }

    private TokenResponseDto authorize(String email, String memberId) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(email, memberId));
        long now = (new Date()).getTime();

        if (refreshTokenRepository.existsByKey(email)) {

            String foundRefreshTokenValue = refreshTokenRepository.findByKey(email).getValue();

            if(jwtProvider.validateToken(foundRefreshTokenValue)) {
                return new TokenResponseDto(
                        jwtProvider.generateAccessToken(authentication, now),
                        foundRefreshTokenValue
                );
            }
        }

        return firstSignIn(authentication);
    }

    private RefreshToken toRefreshToken(String key, String value) {
        return RefreshToken.builder()
                .key(key)
                .value(value)
                .build();
    }

    private TokenResponseDto firstSignIn(Authentication authentication) {
        TokenDto generatedTokenDto = jwtProvider.generateTokenDto(authentication);
        String generatedAccessToken = generatedTokenDto.getAccessToken();
        String generatedRefreshToken = generatedTokenDto.getRefreshToken();

        refreshTokenRepository.save(toRefreshToken(authentication.getName(), generatedRefreshToken));
        return new TokenResponseDto(generatedAccessToken, generatedRefreshToken);
    }
}
