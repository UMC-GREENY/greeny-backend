package greeny.backend.domain.member.service;

import greeny.backend.config.jwt.JwtProvider;
import greeny.backend.domain.member.dto.sign.common.TokenDto;
import greeny.backend.domain.member.dto.sign.common.TokenRequestDto;
import greeny.backend.domain.member.dto.sign.common.TokenResponseDto;
import greeny.backend.domain.member.dto.sign.general.FindPasswordRequestDto;
import greeny.backend.domain.member.dto.sign.general.GetIsAutoInfoResponseDto;
import greeny.backend.domain.member.dto.sign.general.LoginRequestDto;
import greeny.backend.domain.member.dto.sign.general.SignUpRequestDto;
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

    public void validateSignUpInfoWithGeneral(String email) {    // 소셜 로그인에 사용한 이메일이 이미 일반 회원가입된 이메일인지 검증
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfoWithGeneral(signUpRequestDto.getEmail());
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

    public TokenResponseDto signInWithSocial(Provider provider, String email) {  // 소셜 제공자와 사용자 프로필 정보 중 email 을 받아 소셜 로그인

        if(memberRepository.existsByEmail(email)) {  // DB에 이미 존재하는 사용자일 경우
            validateSignUpInfoWithSocial(email);  // 소셜 로그인에 사용한 이메일이 이미 일반 회원가입된 이메일인지 검증
            return authorize(email, getMember(email).getId().toString());  // 토큰 발행
        }

        // 최초 소셜 로그인을 이용하는 사용자일 경우
        Member savedMember = saveSocialMember(provider, email);
        return authorize(email, savedMember.getId().toString());
    }

    @Transactional
    public void findPassword(FindPasswordRequestDto findPasswordRequestDto) {
        getMemberGeneral(getMember(findPasswordRequestDto.getEmail()))
                .changePassword(passwordEncoder.encode(findPasswordRequestDto.getPassword()));
    }

    public GetIsAutoInfoResponseDto autoSignIn(Member member) {
        return new GetIsAutoInfoResponseDto(getMemberGeneral(member).getIsAuto());
    }

    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {

        Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());
        String email = authentication.getName();

        if (!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            refreshTokenRepository.delete(getRefreshToken(email));
            return publishToken(authentication);
        } else {
            validateRefreshTokenOwner(email, tokenRequestDto.getRefreshToken());
            return new TokenResponseDto(
                    jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                    tokenRequestDto.getRefreshToken()
            );
        }
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

    private MemberSocial toMemberSocial(Provider provider, Member member) {
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

    private Member saveSocialMember(Provider provider, String email) {
        Member savedMember = memberRepository.save(toMember(email));
        memberSocialRepository.save(toMemberSocial(provider, savedMember));
        return savedMember;
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

    private TokenResponseDto authorize(String email, String memberId) {  // 여러 가지 경우에 대한 토큰 발행
        // CustomUserDetailsService 의 loadByUsername() 실행, 로그인을 시도하는 사용자의 email, memberId 와 DB에 있는 email, memberId 비교
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(email, memberId));

        if (refreshTokenRepository.existsByKey(email)) {  // 해당 이메일에 대한 refresh token 이 이미 존재할 경우

            RefreshToken foundRefreshToken = getRefreshToken(email);
            String foundRefreshTokenValue = foundRefreshToken.getValue();

            if (jwtProvider.validateToken(foundRefreshTokenValue)) {  // DB 에서 가져온 refresh token 이 유효한지 검증
                return new TokenResponseDto(
                        jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                        foundRefreshTokenValue
                );
            } else {
                refreshTokenRepository.delete(foundRefreshToken);
            }
        }

        return publishToken(authentication);
    }

    private void validateSignUpInfoWithSocial(String email) {  // 소셜 로그인에 사용한 이메일이 이미 일반 회원가입된 이메일인지 검증
        if(memberGeneralRepository.existsByMember(getMember(email))) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private RefreshToken getRefreshToken(String email) {
        return refreshTokenRepository.findByKey(email).orElseThrow(RefreshTokenNotFoundException::new);
    }

    private RefreshToken toRefreshToken(String key, String value) {
        return RefreshToken.builder()
                .key(key)
                .value(value)
                .build();
    }

    private TokenResponseDto publishToken(Authentication authentication) {  // 로그인이 처음 or refresh token 유효하지 않을 때 토큰 발행
        TokenDto generatedTokenDto = jwtProvider.generateTokenDto(authentication);
        String generatedRefreshToken = generatedTokenDto.getRefreshToken();
        refreshTokenRepository.save(toRefreshToken(authentication.getName(), generatedRefreshToken));

        return new TokenResponseDto(generatedTokenDto.getAccessToken(), generatedRefreshToken);
    }

    private void validateRefreshTokenOwner(String email, String refreshToken) {
        if (
                !refreshTokenRepository.findByKey(email)
                        .orElseThrow(RefreshTokenNotFoundException::new).getValue()
                        .equals(refreshToken)
        ) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}