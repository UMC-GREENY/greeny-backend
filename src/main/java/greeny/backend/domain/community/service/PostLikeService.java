package greeny.backend.domain.community.service;

import greeny.backend.domain.community.entity.Post;
import greeny.backend.domain.community.entity.PostLike;
import greeny.backend.domain.community.repository.PostLikeRepository;
import greeny.backend.domain.community.repository.PostRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.PostNotFoundException;
import greeny.backend.exception.situation.SelfLikeNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void like(Long postId, Member liker) {
        boolean retry;
        do {
            try {
                retry = false;
                Optional<PostLike> foundPostLike = findPostLike(postId, liker.getId());
                toggle(postId, liker, foundPostLike);
            }
            catch (OptimisticLockingFailureException e) {
                retry = true;
            }
        } while (retry);
    }

    public void create(Long postId, Member liker) {
        Post post = getPost(postId);
        if (post.getWriter().getId().equals(liker.getId()))
            throw new SelfLikeNotAllowedException();
        postLikeRepository.save(PostLike.builder()
                .post(post)
                .liker(liker)
                .build());
    }

    public Post getPost(Long postId) {
        return postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
    }

    public Optional<PostLike> findPostLike(Long postId, Long likerId) {
        return postLikeRepository.findByPostIdAndLikerId(postId, likerId);
    }

    private void delete(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    private void toggle(Long postId, Member liker, Optional<PostLike> postLike) {
        if (postLike.isEmpty()) {
            create(postId, liker);
            return;
        }
        delete(postLike.get());
    }
}
