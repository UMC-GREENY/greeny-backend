package greeny.backend.domain.community.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "liker_id"})  // 동시성 문제로 인한 DB 중복 저장 방지
})
public class PostLike extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id", nullable = false)
    private Member liker;

}
