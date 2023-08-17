package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.MemberAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAgreementRepository extends JpaRepository<MemberAgreement, Long> {
    Optional<MemberAgreement> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);
}
