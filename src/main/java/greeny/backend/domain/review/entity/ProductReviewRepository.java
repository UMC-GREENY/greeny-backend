package greeny.backend.domain.review.entity;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findProductReviewsByProduct(Pageable pageable,Product product);

    @EntityGraph(attributePaths = "product")
    Page<ProductReview> findAllByReviewer(Pageable pageable, Member member);

    @EntityGraph(attributePaths = "product")
    Page<ProductReview> findAllByContentContainingIgnoreCase(String content, Pageable pageable);

    @EntityGraph(attributePaths = "product")
    @NotNull
    Page<ProductReview> findAll(@NotNull Pageable pageable);
}