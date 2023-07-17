package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.MemberSocial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialRepository extends JpaRepository<MemberSocial, Long> {
}
