package greeny.backend.domain.member.service;

import greeny.backend.config.jwt.JwtProvider;
import greeny.backend.domain.member.dto.sign.common.*;
import greeny.backend.domain.member.dto.sign.general.FindPasswordRequestDto;
import greeny.backend.domain.member.dto.sign.general.GetIsAutoInfoResponseDto;
import greeny.backend.domain.member.dto.sign.general.LoginRequestDto;
import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.member.repository.*;
import greeny.backend.exception.situation.*;
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

    public void validateSignUpInfoWithGeneral(String email) {  // 이미 가입된 이메일인지 검증
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private Member validateSignUpInfoWithSocial(String email) {  // DB에 이미 존재하는 이메일일 때, 일반 로그인 이메일인지 검증
        Member foundMember = getMember(email);

        if(memberGeneralRepository.existsByMember(foundMember)) {
            throw new EmailAlreadyExistsException(email);
        }

        return foundMember;
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {  // 일반 회원가입
        validateSignUpInfoWithGeneral(signUpRequestDto.getEmail());  // 이미 가입된 이메일인지 검증
        saveGeneralMember(signUpRequestDto);
    }

    @Transactional
    public TokenResponseDto signInWithGeneral(LoginRequestDto loginRequestDto) {  // 일반 로그인

        String email = loginRequestDto.getEmail();
        Member foundMember = getMember(email);

        MemberGeneral foundMemberGeneral = getMemberGeneral(foundMember);
        String foundIsAuto = foundMemberGeneral.getIsAuto();

        changeIsAutoBySignIn(loginRequestDto.getIsAuto(), foundMemberGeneral, foundIsAuto);

        return authorize(email, foundMember.getId().toString());
    }

    public TokenResponseDto signInWithSocial(String email, Provider provider) {  // 소셜 제공자와 사용자 프로필 정보 중 email 을 받아 소셜 로그인

        if(memberRepository.existsByEmail(email)) {  // 소셜 로그인 이용이 최초가 아닌 경우

            Member validatedMember = validateSignUpInfoWithSocial(email);// DB에 이미 존재하는 이메일일 때, 일반 로그인 이메일인지 검증
            TokenResponseDto authorizedToken = authorize(email, validatedMember.getId().toString());  // 토큰 발행

            return TokenResponseDto.from(authorizedToken.getAccessToken(), authorizedToken.getRefreshToken());
        }

        saveSocialMemberExceptAgreement(email, provider);  // DB에 소셜 회원 저장
        return TokenResponseDto.excludeTokenInDto(email);
    }

    public TokenResponseDto agreementInSignUp(AgreementRequestDto agreementRequestDto) {  // 일반 회원가입 or 최초 소셜 로그인 이후 동의 항목 여부를 DB에 저장

        String email = agreementRequestDto.getEmail();
        Member foundMember = getMember(email);

        if(!memberRepository.existsByEmail(email)) {  // DB에 이메일이 없는 경우
            throw new MemberNotFoundException();
        }

        saveMemberAgreement(foundMember, agreementRequestDto);  // DB에 Member 동의 여부 저장
        return authorize(email, foundMember.getId().toString());
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

        String refreshToken = tokenRequestDto.getRefreshToken();
        Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());
        String email = authentication.getName();

        if (!jwtProvider.validateToken(refreshToken)) {
            refreshTokenRepository.delete(getRefreshToken(email));
            return publishToken(authentication);
        } else {
            validateRefreshTokenOwner(email, refreshToken);
            return TokenResponseDto.excludeEmailInDto(
                    jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                    refreshToken
            );
        }
    }

    private void saveGeneralMember(SignUpRequestDto signUpRequestDto) {  // 일반 회원 DB에 저장
        Member savedMember = memberRepository.save(toMember(signUpRequestDto.getEmail()));
        memberGeneralRepository.save(toMemberGeneral(savedMember, signUpRequestDto.getPassword(), passwordEncoder));
        memberProfileRepository.save(
                toMemberProfile(
                        savedMember,
                        signUpRequestDto.getName(),
                        signUpRequestDto.getPhone(),
                        signUpRequestDto.getBirth()
                )
        );
    }

    private void saveSocialMemberExceptAgreement(String email, Provider provider) {  // 소셜 회원 DB에 저장
        Member savedMember = memberRepository.save(toMember(email));
        memberSocialRepository.save(toMemberSocial(provider, savedMember));
    }

    private void saveMemberAgreement(Member member, AgreementRequestDto agreementRequestDto) {  // 일반 회원가입 or 최초 소셜 로그인 시 동의 항목 여부 DB에 저장
        memberAgreementRepository.save(
                agreementRequestDto.toMemberAgreement(
                        member,
                        agreementRequestDto.getPersonalInfo(),
                        agreementRequestDto.getThirdParty()
                )
        );
    }

    private Member toMember(String email) {  // Member 객체로 변환
        return Member.builder()
                .email(email)
                .role(Role.ROLE_USER)
                .build();
    }

    private MemberGeneral toMemberGeneral(Member member, String password, PasswordEncoder passwordEncoder) {  // MemberGeneral 객체로 변환
        return MemberGeneral.builder()
                .member(member)
                .password(passwordEncoder.encode(password))
                .isAuto("0")
                .build();
    }

    private MemberSocial toMemberSocial(Provider provider, Member member) {  // MemberSocial 객체로 변환
        return MemberSocial.builder()
                .member(member)
                .provider(provider)
                .build();
    }

    private MemberProfile toMemberProfile(Member member, String name, String phone, String birth) {  // MemberProfile 객체로 변환
        return MemberProfile.builder()
                .member(member)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
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
                return TokenResponseDto.excludeEmailInDto(
                        jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                        foundRefreshTokenValue
                );
            } else {
                refreshTokenRepository.delete(foundRefreshToken);
            }
        }

        return publishToken(authentication);
    }

    private RefreshToken getRefreshToken(String email) {
        return refreshTokenRepository.findByKey(email)
                .orElseThrow(RefreshTokenNotFoundException::new);
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

        return TokenResponseDto.excludeEmailInDto(generatedTokenDto.getAccessToken(), generatedRefreshToken);
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