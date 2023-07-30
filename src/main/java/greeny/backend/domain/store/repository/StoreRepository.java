package greeny.backend.domain.store.repository;

import greeny.backend.domain.store.entity.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store s left join fetch s.bookmarks left join fetch s.reviews")  // Collection fetch join -> DB 과부하 방지를 위해 batch size 설정, @Query 를 통해 메소드 네이밍 커스텀
    @NotNull
    List<Store> findStoresWithBookmarksAndReviews();
}
