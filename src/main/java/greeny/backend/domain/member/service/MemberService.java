package greeny.backend.domain.member.service;


import greeny.backend.domain.member.dto.member.EditMemberInfoRequestDto;
import greeny.backend.domain.member.dto.member.GetMemberInfoResponseDto;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.repository.MemberRepository;
import greeny.backend.domain.member.repository.RefreshTokenRepository;
import greeny.backend.exception.situation.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Member getCurrentMember() {
        return memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    public GetMemberInfoResponseDto getCurrentMemberInfo() {
        return GetMemberInfoResponseDto.toDto(getCurrentMember());
    }

    public void deleteMember() {
        Member currentMember = getCurrentMember();
        // 우리 회원이 맞는지 DB에 확인을 하고 경우에 따라서 예외 처리하기


        memberRepository.delete(currentMember);
    }

    @Transactional
    public void editMemberInfo(EditMemberInfoRequestDto editMemberRequestDto) {
        //현재 비밀번호를 입력받아   서 회원 맞는지 체크 하기
//        editMemberRequestDto.getPasswordToCheck()

        //맞다면 바꾸려는 비밀번호로 변경하    기 (db에 넣고)
//        editMemberRequestDto.getPasswordToChange()

        //틀리다면 틀리다는 메세지를 넘겨주고 예외처리 해주기
        // CF.애초에 입력값이 없거나 조건에 맞지 않는다면 dto 어노테이션이서 필터링 되기에 신경쓰지 않아도 됩니다.


    }



    public Member getCurrentMember(){
        return memberRepository.findByEmail()// 입력받은 이메일이 맞는 회원을 db에서 찾아서 멤버 객체를 반환해주기
    }

}
