package greeny.backend.domain.review.repository;

import greeny.backend.domain.review.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
}
