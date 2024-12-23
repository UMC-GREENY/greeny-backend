package greeny.backend.domain.community.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndLikerId(Long postId, Long likerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PostLike> findByPostIdAndLiker(Long postId, Member liker);
}