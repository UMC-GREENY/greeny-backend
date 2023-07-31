package greeny.backend.domain.community.controller;

import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.community.dto.WritePostRequestDto;
import greeny.backend.domain.community.service.PostService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "Post API Document")
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @Operation(summary = "Write post api", description = "put your post info to write. you can skip files.")
    @ResponseStatus(OK)
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Response writePost(@Valid @RequestPart(name = "body(json)") WritePostRequestDto writePostRequestDto,
                               @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {
        postService.writePost(writePostRequestDto, multipartFiles, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_POST);
    }

    @Operation(summary = "Search post list api", description = "put keyword and page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/search")
    public Response searchPostList(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_SEARCH_POST_LIST, postService.searchPostList(keyword, pageable));
    }

    @Operation(summary = "Get post api", description = "put post id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getPost(Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPost(postId));
    }

    @Operation(summary = "Delete post API", description = "put post id what you want to delete")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deletePost(Long postId){
        postService.deletePost(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_POST);
    }
    @Operation(summary = "Edit post api", description = "put post info what you want to edit.")
    @ResponseStatus(OK)
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Response editPost(Long postId,
                             @Valid @RequestPart(name = "body(json)") WritePostRequestDto editPostRequestDto,
                             @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles){
        postService.editPost(postId, editPostRequestDto, multipartFiles, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_EDIT_POST);
    }
}
