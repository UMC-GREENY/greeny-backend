package greeny.backend.domain.member.service;


import greeny.backend.domain.member.dto.member.EditMemberInfoRequestDto;
import greeny.backend.domain.member.dto.member.GetMemberInfoResponseDto;
import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.member.repository.*;
import greeny.backend.exception.situation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public Member getCurrentMember() {  // 스프링 시큐리티 컨텍스트에서 사용자 정보 가져오기
        return memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    public GetMemberInfoResponseDto getMemberInfo() {  //회원 정보 가져오기

        Member currentMember = getCurrentMember();
        MemberProfile currentMemberInfo = getMemberProfile(currentMember);

        if(memberGeneralRepository.existsByMember(currentMember)) {
            return GetMemberInfoResponseDto.toGeneralMemberDto(
                    currentMember.getEmail(),
                    currentMemberInfo.getName(),
                    currentMemberInfo.getPhone(),
                    currentMemberInfo.getBirth()
            );
        }

        return GetMemberInfoResponseDto.toSocialMemberDto(getMemberSocial(currentMember).getProvider().getName());
    }

    public void deleteMember() {

        Member currentMember = getCurrentMember();
        String key = currentMember.getEmail();

        if(refreshTokenRepository.existsById(key)) {  // 리프레쉬 토큰이 남아있는지
            refreshTokenRepository.deleteById(key);
        }

        memberRepository.delete(currentMember);
    }

    @Transactional
    public void editMemberInfo(EditMemberInfoRequestDto editMemberRequestDto) {  // 비밀번호 변경

        MemberGeneral currentGeneralMember = memberGeneralRepository.findByMember(getCurrentMember())
                .orElseThrow(MemberGeneralNotFoundException::new);

        //현재 비밀번호를 입력받아서 회원 맞는지 체크 하기
        if(!passwordEncoder.matches(editMemberRequestDto.getPasswordToCheck(), currentGeneralMember.getPassword())) {
            throw new MemberNotEqualsException();
        }

        currentGeneralMember.changePassword(passwordEncoder.encode(editMemberRequestDto.getPasswordToChange()));  // 맞으면 변경
    }

    private MemberProfile getMemberProfile(Member member) {
        return memberProfileRepository.findByMember(member)
                .orElseThrow(MemberProfileNotFoundException::new);
    }
    private MemberSocial getMemberSocial(Member member) {
        return memberSocialRepository.findByMember(member)
                .orElseThrow(MemberSocialNotFoundException::new);
    }
}
