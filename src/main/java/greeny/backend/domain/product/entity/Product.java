package greeny.backend.domain.product.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.store.entity.Store;
import lombok.*;
import org.hibernate.annotations.Formula;
import javax.persistence.*;
import java.util.*;
import static javax.persistence.CascadeType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Product extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private Set<ProductBookmark> productBookmarks = new HashSet<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int deliveryFee;

    @Column(nullable = false)
    private int price;

    private String contentUrl;

    @Formula("(select count(*) from product_bookmark pb where pb.product_id = product_id)")
    private int bookmarks;

    @Formula("(select count(*) from product_review pr where pr.product_id = product_id)")
    private int reviews;
}