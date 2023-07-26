package greeny.backend.domain.review.repository;

import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.reviewimage.entity.ProductReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

}
