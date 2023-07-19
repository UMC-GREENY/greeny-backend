package greeny.backend.domain.post.controller;

import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.dto.GetPostResponseDto;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.service.PostService;
import greeny.backend.exception.situation.MemberNotEqualsException;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static greeny.backend.domain.post.SuccessMessage.*;
import static greeny.backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create post api", description = "put your post info to create. you can skip files.")
    @ResponseStatus(OK)
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public Response createPost(@Valid @RequestPart(name = "body(json)") CreatePostRequestDto createPostRequestDto,
                               @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {
        postService.creatPost(createPostRequestDto, multipartFiles);
//        postService.creatPost(createPostRequestDto, multipartFiles, memberService.getCurrentMember());
        return success(SUCCESS_TO_CREATE_POST);
    }

    @Operation(summary = "Search post list api", description = "put keyword and page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/search")
    public Response searchPostList(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_SEARCH_POST_LIST, postService.searchPosts(keyword, pageable));
    }

    @Operation(summary = "Get post api", description = "put post id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getPost(@RequestParam Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPost(postId));
    }

    @Operation(summary = "Delete post API", description = "put post id what you want to delete")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deletePost(@RequestParam Long postId){
        postService.deletePost(postId);
        return Response.success(SUCCESS_TO_DELETE_POST);
    }

}
