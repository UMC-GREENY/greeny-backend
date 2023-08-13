package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)  // 영속성 컨텍스트 flush & clear 적용
    @Query("delete from ProductBookmark pb where pb.id in :ids")
    void deleteProductBookmarksByIds(@Param("ids") List<Long> ids);
    @EntityGraph(attributePaths = {"product"})
    List<ProductBookmark> findProductBookmarksByLiker(Member liker);
    Optional<ProductBookmark> findByProductIdAndLikerId(Long productId, Long likerId);

    @EntityGraph(attributePaths = {"product"})
    Page<ProductBookmark> findProductBookmarksByLiker(Pageable pageable , Member liker); //Member에 대한 Product bookmark 리스트 가져오기
}
