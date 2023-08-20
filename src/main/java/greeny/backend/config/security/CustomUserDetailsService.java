package greeny.backend.config.security;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.repository.MemberGeneralRepository;
import greeny.backend.domain.member.repository.MemberRepository;
import greeny.backend.domain.member.repository.MemberSocialRepository;
import greeny.backend.exception.situation.MemberGeneralNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(passwordEncoder.encode(member.getId().toString()))
                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .build();
    }
}
