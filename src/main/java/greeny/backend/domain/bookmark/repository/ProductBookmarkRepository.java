package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
    @EntityGraph(attributePaths = {"product"})
    List<ProductBookmark> findProductBookmarksByLiker(Member liker);
    Optional<ProductBookmark> findByProductIdAndLikerId(Long productId, Long likerId);
}
