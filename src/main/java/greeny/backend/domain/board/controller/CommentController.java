package greeny.backend.domain.board.controller;

import greeny.backend.domain.board.dto.CreateCommentRequestDto;
import greeny.backend.domain.board.dto.CreatePostRequestDto;
import greeny.backend.domain.board.dto.UpdatePostRequestDto;
import greeny.backend.domain.board.service.CommentService;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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

}
