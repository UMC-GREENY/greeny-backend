package greeny.backend.domain.community.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndLikerId(Long postId, Long likerId);
    Optional<PostLike> findByPostIdAndLiker(Long postId, Member liker);
}