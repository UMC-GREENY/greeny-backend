package greeny.backend.domain.board.controller;

import greeny.backend.domain.board.dto.CreateCommentRequestDto;
import greeny.backend.domain.board.service.CommentService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "Comment API Document")
@RequestMapping(value = "/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @Operation(summary = "Create comment api", description = "put your comment info to create.")
    @ResponseStatus(OK)
    @PostMapping
    public Response createComment(@RequestParam Long postId, @Valid @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        commentService.creatComment(postId, createCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_CREATE_COMMENT);
    }

    @Operation(summary = "Get comment list api", description = "put post id to get comment list.")
    @ResponseStatus(OK)
    @GetMapping
    public Response getCommentList(@RequestParam Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getComments(postId));
    }

    @Operation(summary = "Update comment api", description = "put your comment info to update.")
    @ResponseStatus(OK)
    @PutMapping
    public Response updateComment(@RequestParam Long commentId, @Valid @RequestBody CreateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(commentId, updateCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_UPDATE_COMMENT);
    }

}
