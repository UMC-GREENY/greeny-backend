package greeny.backend.domain.review.repository;

import greeny.backend.domain.review.entity.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {


}
