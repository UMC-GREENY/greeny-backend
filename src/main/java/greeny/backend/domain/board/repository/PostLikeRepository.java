package greeny.backend.domain.board.repository;

import greeny.backend.domain.board.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndLikerId(Long postId, Long likerId);
}
