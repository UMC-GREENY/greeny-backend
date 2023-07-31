package greeny.backend.domain.community.repository;

import greeny.backend.domain.community.entity.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @NotNull
    Page<Post> findAll(@NotNull Pageable pageable);
    //제목 검색 -> 제목+내용 검색. IgnoreCase : 대소문자 구별 무시
    Page<Post> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
    @NotNull
    Optional<Post> findById(@NotNull Long id);
    Page<Post> findAllByWriterId(Long writerId, Pageable pageable);
}
