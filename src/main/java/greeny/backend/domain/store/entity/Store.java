package greeny.backend.domain.store.entity;


import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.StoreReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private List<StoreReview> storeReviews = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private Set<StoreBookmark> storeBookmarks = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private String name;
    private String webUrl;
    @Column(nullable = false)
    private String location;
    private String phone;
    private String imageUrl;
    private String runningTime;

    @Formula("(select count(1) from store_bookmark sb where sb.store_id = store_id)")
    private int bookmarks;  // 찜 개수를 나타내는 가성 컬럼 (DB 에는 나타나지 x)
    @Formula("(select count(1) from store_review sr where sr.store_id = store_id)")
    private int reviews;  // 리뷰 수를 나타내는 가성 컬럼 (DB 에는 나타나지 x)
}
