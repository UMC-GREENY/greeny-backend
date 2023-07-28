package greeny.backend.domain.board.repository;

import greeny.backend.domain.board.entity.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"writer", "postFiles"}) // writer와 postFiles을 fetch join으로 한번에 조회
    @NotNull Page<Post> findAll(@NotNull Pageable pageable);

    //제목 검색 -> 제목+내용 검색. IgnoreCase : 대소문자 구별 무시
    @EntityGraph(attributePaths = {"writer", "postFiles"})
    Page<Post> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    @EntityGraph(attributePaths = {"writer", "postFiles"})
    @NotNull Optional<Post> findById(@NotNull Long id);

    @EntityGraph(attributePaths = {"writer", "postFiles"})
    Page<Post> findAllByWriterId(Long writerId, Pageable pageable);
}
