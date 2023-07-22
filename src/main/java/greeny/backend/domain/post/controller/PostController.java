package greeny.backend.domain.post.controller;

import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.post.dto.UpdatePostRequestDto;
import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.service.PostService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @Operation(summary = "Create post api", description = "put your post info to create. you can skip files.")
    @ResponseStatus(OK)
    @PostMapping(path = "/posts", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public Response createPost(@Valid @RequestPart(name = "body(json)") CreatePostRequestDto createPostRequestDto,
                               @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {
        postService.creatPost(createPostRequestDto, multipartFiles, memberService.getCurrentMember());
        return success(SUCCESS_TO_CREATE_POST);
    }

    @Operation(summary = "Search post list api", description = "put keyword and page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("posts/search")
    public Response searchPostList(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_SEARCH_POST_LIST, postService.searchPosts(keyword, pageable));
    }

    @Operation(summary = "Get post api", description = "put post id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("posts")
    public Response getPost(@RequestParam Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPost(postId));
    }

    @Operation(summary = "Delete post API", description = "put post id what you want to delete")
    @ResponseStatus(OK)
    @DeleteMapping("posts")
    public Response deletePost(@RequestParam Long postId){
        postService.deletePost(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_POST);
    }
    @Operation(summary = "Update post api", description = "put post info what you want to update.")
    @ResponseStatus(OK)
    @PutMapping(path = "posts", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public Response updatePost(@RequestParam Long postId,
                               @Valid @RequestPart(name = "body(json)") UpdatePostRequestDto updatePostRequestDto,
                               @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles){
        memberService.getCurrentMember();
        postService.updatePost(postId, updatePostRequestDto, multipartFiles, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_UPDATE_POST);
    }

}
