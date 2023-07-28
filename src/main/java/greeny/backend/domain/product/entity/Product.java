package greeny.backend.domain.product.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductReview> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private Set<ProductBookmark> bookmarks = new HashSet<>();

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private int deliveryFee;
    @Column(nullable = false)
    private int price;
}
