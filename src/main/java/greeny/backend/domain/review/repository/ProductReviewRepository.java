package greeny.backend.domain.review.repository;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.ProductReview;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findProductReviewsByProduct(Pageable pageable,Product product);
    Page<ProductReview> findAllByReviewer(Pageable pageable, Member member);
    Page<ProductReview> findAllByContentContainingIgnoreCase(String content, Pageable pageable);
    @NotNull
    Page<ProductReview> findAll(@NotNull Pageable pageable);
}
