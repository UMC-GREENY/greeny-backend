package greeny.backend.domain.store.repository;

import greeny.backend.domain.store.entity.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findTop3ByOrderByIdDesc();  // Id 내림차순으로 3개의 store 조회
    List<Store> findTop8ByOrderByBookmarksDesc();  // Bookmark 내림차순으로 8개의 store 조회, 같을 경우에는 id 내림차순으로 조회
    Page<Store> findStoresByOrderByBookmarksDesc(Pageable pageable);  // Bookmark 내림차순으로 정렬하여 조회
    Page<Store> findStoresByOrderByReviewsDesc(Pageable pageable);  // Review 내림차순으로 정렬하여 조회
}
