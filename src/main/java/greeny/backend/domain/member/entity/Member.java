package greeny.backend.domain.member.entity;

import greeny.backend.domain.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends AuditEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @OneToOne(mappedBy = "member", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private MemberGeneral memberGeneral;
    @OneToOne(mappedBy = "member", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private MemberSocial memberSocial;
    @OneToOne(mappedBy = "member", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private MemberProfile memberProfile;
    @OneToOne(mappedBy = "member", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private MemberAgreement memberAgreement;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Role role;
}
