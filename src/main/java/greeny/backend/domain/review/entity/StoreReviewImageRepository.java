package greeny.backend.domain.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoreReviewImageRepository extends JpaRepository<StoreReviewImage, Long> {
    List<StoreReviewImage> findByStoreReviewId(Long reviewId);
}