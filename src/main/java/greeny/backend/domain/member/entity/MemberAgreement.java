package greeny.backend.domain.member.entity;

import greeny.backend.domain.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAgreement extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_agreement_id")
    private Long id;
    private Long memberId;
    @Column(nullable = false)
    private boolean personalInfo;
    @Column(nullable = false)
    private boolean thirdParty;
}
