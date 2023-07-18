package greeny.backend.domain.member.service;

import greeny.backend.config.jwt.JwtProvider;
import greeny.backend.domain.member.dto.sign.SignUpRequestDto;
import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.member.repository.*;
import greeny.backend.exception.situation.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberAgreementRepository memberAgreementRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public void validateSignUpInfo(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfo(signUpRequestDto.getEmail());
        saveGeneralMember(signUpRequestDto);
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
}
