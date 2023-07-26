package greeny.backend.domain.board.service;

import greeny.backend.domain.board.dto.WriteCommentRequestDto;
import greeny.backend.domain.board.dto.GetCommentListResponseDto;
import greeny.backend.domain.board.entity.Comment;
import greeny.backend.domain.board.entity.Post;
import greeny.backend.domain.board.repository.CommentRepository;
import greeny.backend.domain.board.repository.PostRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.CommentNotFoundException;
import greeny.backend.exception.situation.MemberNotEqualsException;
import greeny.backend.exception.situation.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void writeComment(Long postId, WriteCommentRequestDto writeCommentRequestDto, Member writer) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        commentRepository.save(writeCommentRequestDto.toEntity(post ,writer));
    }

    @Transactional(readOnly = true)
    public List<GetCommentListResponseDto> getCommentList(Long postId) {
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return commentRepository.findAllByPostId(postId).stream().
                map(GetCommentListResponseDto::from).
                collect(Collectors.toList());

    }

    @Transactional
    public void editComment(Long commentId, WriteCommentRequestDto editCommentRequestDto, Member currentMember) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if(comment.getWriter().getId() != currentMember.getId()) throw new MemberNotEqualsException();// 작성자가 맞는지 확인
        comment.update(editCommentRequestDto.getContent());
    }

    public void deleteComment(Long commentId, Member currentMember) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if(comment.getWriter().getId() != currentMember.getId()) throw new MemberNotEqualsException();// 작성자가 맞는지 확인
        commentRepository.delete(comment);
    }
}
