package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, Long> {
}
