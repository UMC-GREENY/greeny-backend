package greeny.backend.domain.bookmark.entity;

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

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    Optional<StoreBookmark> findByStoreIdAndLikerId(Long storeId, Long likerId);

    @EntityGraph(attributePaths = {"store"})
    List<StoreBookmark> findStoreBookmarksByLiker(Member liker);

    @EntityGraph(attributePaths = {"store"})
    Page<StoreBookmark> findStoreBookmarksByLiker(Pageable pageable , Member liker);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StoreBookmark sb where sb.id in :ids")
    void deleteStoreBookmarksByIds(@Param("ids") List<Long> ids);
}