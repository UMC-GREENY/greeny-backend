package greeny.backend.domain.board.repository;

import greeny.backend.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"writer"}) // writer를 fetch join으로 한번에 조회
    List<Comment> findAllByPostId(Long postId);
}
