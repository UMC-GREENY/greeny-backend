package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.MemberSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSocialRepository extends JpaRepository<MemberSocial, Long> {
    Optional<MemberSocial> findByMember(Member member);
}
