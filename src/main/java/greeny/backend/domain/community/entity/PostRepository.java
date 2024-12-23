package greeny.backend.domain.community.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"writer"})
    @NotNull Page<Post> findAll(@NotNull Pageable pageable);

    @EntityGraph(attributePaths = {"writer"})
    Page<Post> findAllByWriterId(Long writerId, Pageable pageable);

    @EntityGraph(attributePaths = {"writer"})
    Page<Post> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer WHERE p.id = :id")
    Optional<Post> findByIdWithWriter(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer LEFT JOIN FETCH p.postFiles WHERE p.id = :id")
    Optional<Post> findByIdWithWriterAndPostFiles(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer LEFT JOIN FETCH p.postFiles LEFT JOIN FETCH p.postLikes WHERE p.id = :id")
    Optional<Post> findByIdWithWriterAndPostFilesAndPostLikes(@Param("id") Long id);
}