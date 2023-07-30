package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, Long> {
    Optional<MemberGeneral> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);
}
