package greeny.backend.domain.community.presentation.controller;

import greeny.backend.domain.community.presentation.dto.WriteCommentRequestDto;
import greeny.backend.domain.community.application.CommentService;
import greeny.backend.domain.member.application.MemberService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/comments")
@Tag(name = "Comment", description = "Comment API Document")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @Operation(summary = "Write comment API", description = "put post id and comment info to write.")
    @ResponseStatus(OK)
    @PostMapping
    public Response writeComment(Long postId, @Valid @RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        commentService.writeComment(postId, writeCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_COMMENT);
    }

    @Operation(summary = "Get simple comment infos API", description = "put post id to get comment list.")
    @ResponseStatus(OK)
    @GetMapping
    public Response getSimpleCommentInfos(Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getSimpleCommentInfos(postId));
    }

    @Operation(summary = "Get simple comment infos with auth member API", description = "put post id to get comment list.")
    @ResponseStatus(OK)
    @GetMapping("/auth")
    public Response getSimpleCommentInfosWithAuthMember(Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getSimpleCommentInfosWithAuthMember(postId, memberService.getCurrentMember()));
    }

    @Operation(summary = "Edit comment info API", description = "put comment info to edit.")
    @ResponseStatus(OK)
    @PutMapping
    public Response editCommentInfo(Long commentId, @Valid @RequestBody WriteCommentRequestDto editCommentInfoRequestDto) {
        commentService.editCommentInfo(commentId, editCommentInfoRequestDto, memberService.getCurrentMember());
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