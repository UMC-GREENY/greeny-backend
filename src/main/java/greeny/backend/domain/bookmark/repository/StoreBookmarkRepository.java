package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    @EntityGraph(attributePaths = {"store"})  // fetch join 을 통해 StoreBookmark 객체를 가져옴과 동시에 프로퍼티인 Store 객체도 가져오기
    List<StoreBookmark> findStoreBookmarksByLiker(Member liker);  // Member 에 대한 store bookmark 리스트 가져오기
    @EntityGraph(attributePaths = {"store"})
    Page<StoreBookmark> findStoreBookmarksByLiker(Pageable pageable, Member liker);
}
