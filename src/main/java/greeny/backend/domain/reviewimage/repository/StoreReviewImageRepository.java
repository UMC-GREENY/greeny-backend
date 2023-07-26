package greeny.backend.domain.reviewimage.repository;

import greeny.backend.domain.reviewimage.entity.StoreReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReviewImageRepository extends JpaRepository<StoreReviewImage, Long> {
    public List<Long> findByStoreReview_Id(Long reviewId);
}
