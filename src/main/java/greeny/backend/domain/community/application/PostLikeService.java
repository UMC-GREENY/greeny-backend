package greeny.backend.domain.community.application;

import greeny.backend.domain.community.entity.Post;
import greeny.backend.domain.community.entity.PostLike;
import greeny.backend.domain.community.entity.PostLikeRepository;
import greeny.backend.domain.community.entity.PostRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.PostNotFoundException;
import greeny.backend.exception.situation.SelfLikeNotAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void like(Long postId, Member liker) {
        toggle(postId, liker, findPostLike(postId, liker));
    }

    public Optional<PostLike> findPostLike(Long postId, Member liker) {
        return postLikeRepository.findByPostIdAndLiker(postId, liker);
    }

    public void toggle(Long postId, Member liker, Optional<PostLike> postLike) {
        if (postLike.isEmpty()) {
            create(postId, liker);
            return;
        }
        delete(postLike.get());
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

    private void delete(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    public Post getPost(Long postId) {
        return postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
    }
}