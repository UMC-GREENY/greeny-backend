package greeny.backend.domain.community.repository;

import greeny.backend.domain.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndLikerId(Long postId, Long likerId);
}
