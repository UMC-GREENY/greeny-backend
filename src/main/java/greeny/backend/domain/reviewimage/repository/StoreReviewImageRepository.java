package greeny.backend.domain.reviewimage.repository;

import greeny.backend.domain.reviewimage.entity.StoreReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewImageRepository extends JpaRepository<StoreReviewImage, Long> {
}
