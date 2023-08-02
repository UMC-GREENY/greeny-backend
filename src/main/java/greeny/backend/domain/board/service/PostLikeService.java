package greeny.backend.domain.board.service;

import greeny.backend.domain.board.entity.Post;
import greeny.backend.domain.board.entity.PostLike;
import greeny.backend.domain.board.repository.PostLikeRepository;
import greeny.backend.domain.board.repository.PostRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.PostNotFoundException;
import greeny.backend.exception.situation.SelfLikeNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    private final PostRepository postRepository;

    // 좋아요 or 취소
    public void togglePostLike(Long postId, Member liker) {
        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndLikerId(postId, liker.getId());
        // 이미 좋아요 눌렀는지 확인
        if(postLike.isEmpty()) likePost(postId, liker); // 안 눌렀다면 좋아요 수행
        else postLikeRepository.delete(postLike.get()); // 이미 눌렀다면 취소 수행

    }

    // 게시글 좋아요
    public void likePost(Long postId, Member liker){
        Post post = postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
        if(post.getWriter().getId().equals(liker.getId())) throw new SelfLikeNotAllowedException(); // 자기의 게시글에는 좋아요를 누를 수 없음
        postLikeRepository.save(PostLike.builder()
                .post(post)
                .liker(liker)
                .build());
    }
}
