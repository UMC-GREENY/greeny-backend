package greeny.backend.domain.store.entity;


import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.StoreReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<StoreBookmark> storeBookmarks = new ArrayList<>();

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
}
