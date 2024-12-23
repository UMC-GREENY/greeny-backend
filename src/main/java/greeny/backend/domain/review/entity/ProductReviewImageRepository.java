package greeny.backend.domain.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, Long> {
    List<ProductReviewImage> findByProductReviewId(Long reviewId);
}