package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
}
