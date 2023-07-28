package greeny.backend.domain.store.repository;

import greeny.backend.domain.store.entity.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @EntityGraph(attributePaths = {"reviews", "bookmarks"})  // Collection fetch join -> DB 과부하 방지를 위해 batch size 설정
    @NotNull
    List<Store> findAll();
}
