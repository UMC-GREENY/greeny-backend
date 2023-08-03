package greeny.backend.domain.bookmark.repository;

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

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)  // 영속성 컨텍스트 flush & clear 적용
    @Query("delete from StoreBookmark sb where sb.id in :ids")
    void deleteStoreBookmarksByIds(@Param("ids") List<Long> ids);
    @EntityGraph(attributePaths = {"store"})  // fetch join 을 통해 StoreBookmark 객체를 가져옴과 동시에 프로퍼티인 Store 객체도 가져오기
    List<StoreBookmark> findStoreBookmarksByLiker(Member liker);  // Member 에 대한 store bookmark 리스트 가져오기 @EntityGraph(attributePaths = {"store"})
    Optional<StoreBookmark> findByStoreIdAndLikerId(Long storeId, Long likerId);  // store id, liker id 를 통해 storeBookmark 가져오기
}
