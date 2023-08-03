package greeny.backend.domain.community.repository;

import greeny.backend.domain.community.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"writer"}) // writer를 fetch join으로 한번에 조회
    List<Comment> findAllByPostId(Long postId);
    @Query("SELECT c FROM Comment c JOIN FETCH c.writer WHERE c.id = :id")
    Optional<Comment> findByIdWithWriter(@Param("id") Long id);
}
