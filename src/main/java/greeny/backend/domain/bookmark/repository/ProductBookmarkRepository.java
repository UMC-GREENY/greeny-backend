package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
    @EntityGraph(attributePaths = {"product"})
    List<ProductBookmark> findProductBookmarksByLiker(Member liker);
}
