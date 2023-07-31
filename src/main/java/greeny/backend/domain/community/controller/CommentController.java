package greeny.backend.domain.community.controller;

import greeny.backend.domain.community.dto.WriteCommentRequestDto;
import greeny.backend.domain.community.service.CommentService;
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

    @Operation(summary = "Write comment api", description = "put post id and comment info to write.")
    @ResponseStatus(OK)
    @PostMapping
    public Response writeComment(Long postId, @Valid @RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        commentService.writeComment(postId, writeCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_COMMENT);
    }

    @Operation(summary = "Get comment list api", description = "put post id to get comment list.")
    @ResponseStatus(OK)
    @GetMapping
    public Response getCommentList(Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getCommentList(postId));
    }

    @Operation(summary = "Edit comment api", description = "put comment info to edit.")
    @ResponseStatus(OK)
    @PutMapping
    public Response editComment(Long commentId, @Valid @RequestBody WriteCommentRequestDto editCommentRequestDto) {
        commentService.editComment(commentId, editCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_EDIT_COMMENT);
    }

    @Operation(summary = "Delete comment API", description = "put comment info to delete")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deleteComment(@RequestParam Long commentId){
        commentService.deleteComment(commentId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_COMMENT);
    }

}
