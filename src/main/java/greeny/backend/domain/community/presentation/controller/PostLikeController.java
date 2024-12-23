package greeny.backend.domain.community.presentation.controller;

import greeny.backend.domain.community.application.PostLikeService;
import greeny.backend.domain.member.application.MemberService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts/like")
@Tag(name = "Post Like", description = "Post Like API Document")
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @Operation(summary = "Like or unlike post API", description = "put post id what you want to like or unlike.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response togglePostLike(Long postId) {
        postLikeService.like(postId, memberService.getCurrentMember());
        return success(SUCCESS_TO_TOGGLE_POST_LIKE);
    }
}