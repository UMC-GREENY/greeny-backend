package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {
}
