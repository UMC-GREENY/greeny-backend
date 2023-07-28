package greeny.backend.domain.review.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.reviewimage.entity.StoreReviewImage;
import greeny.backend.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreReview extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_review_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;
    @OneToMany(mappedBy = "storeReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default //생략시 builder 패턴 사용시 초기값(null)으로 설정됨
    private List<StoreReviewImage> storeReviewImages = new ArrayList<>();

    @Column(nullable = false)
    private int star;
    @Column(nullable = false)
    private String content;


}
