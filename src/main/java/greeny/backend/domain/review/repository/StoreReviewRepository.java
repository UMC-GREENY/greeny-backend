package greeny.backend.domain.review.repository;

import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.store.entity.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    Page<StoreReview> findStoreReviewsByStore(Pageable pageable, Store store);
    @NotNull
    Page<StoreReview> findAll(@NotNull Pageable pageable);
}
