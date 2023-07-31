package greeny.backend.domain.review.repository;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.ProductReview;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findProductReviewsByProduct(Pageable pageable,Product product);
    Page<ProductReview> findProductReviewsByReviewer(Pageable pageable,Member member);
    @NotNull
    Page<ProductReview> findAll(@NotNull Pageable pageable);
}
