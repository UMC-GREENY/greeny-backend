package greeny.backend.domain.reviewimage.repository;

import greeny.backend.domain.reviewimage.entity.ProductReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, Long> {
    public List<Long> findByProductReview_Id(Long reviewId);
}
