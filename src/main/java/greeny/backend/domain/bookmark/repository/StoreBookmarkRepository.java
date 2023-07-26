package greeny.backend.domain.bookmark.repository;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    @EntityGraph(attributePaths = {"store"})
    List<StoreBookmark> findStoreBookmarksByLiker(Member liker);
    @EntityGraph(attributePaths = {"store"})
    Page<StoreBookmark> findStoreBookmarksByLiker(Pageable pageable, Member liker);
}
