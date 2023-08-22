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
    Page<Store> findStoresByNameContainingIgnoreCase(String keyword, Pageable pageable);  // 검색어를 포함하는 이름을 가진 스토어 목록 가져오기
    Page<Store> findStoresByLocationContainingIgnoreCase(String location, Pageable pageable);
    Page<Store> findStoresByCategoryContainingIgnoreCase(String category, Pageable pageable);
    Page<Store> findStoresByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String keyword, String location, Pageable pageable);
    Page<Store> findStoresByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String keyword, String category, Pageable pageable);
    Page<Store> findStoresByLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(String location, String category, Pageable pageable);
    Page<Store> findStoresByNameContainingIgnoreCaseAndLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(String keyword, String location, String category, Pageable pageable);
}
