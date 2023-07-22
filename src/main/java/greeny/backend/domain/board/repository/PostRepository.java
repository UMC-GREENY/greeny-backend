package greeny.backend.domain.board.repository;

import greeny.backend.domain.board.entity.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @NotNull
    Page<Post> findAll(@NotNull Pageable pageable);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
    @EntityGraph(attributePaths = {"postFiles"})
    @NotNull
    Optional<Post> findById(@NotNull Long id);
    Page<Post> findByWriterId(Long writerId, Pageable pageable);
}
