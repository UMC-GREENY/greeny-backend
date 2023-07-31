package greeny.backend.domain.board.controller;

import greeny.backend.domain.board.service.PostLikeService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_TOGGLE_POST_LIKE;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Post Like", description = "Post Like API Document")
@RequestMapping(value = "/api/posts/like")
public class PostLikeController {

    private final PostLikeService postLikeService;

    private final MemberService memberService;

    // 게시글 좋아요 or 취소 API
    @Operation(summary = "Like or unlike post API", description = "put post id what you want to like or unlike.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response togglePostLike(Long postId) {
        postLikeService.togglePostLike(postId, memberService.getCurrentMember());
        return success(SUCCESS_TO_TOGGLE_POST_LIKE);
    }

}
