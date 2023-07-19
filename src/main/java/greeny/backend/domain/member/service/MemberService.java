package greeny.backend.domain.member.service;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.repository.MemberRepository;
import greeny.backend.domain.member.repository.RefreshTokenRepository;
import greeny.backend.exception.situation.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}
