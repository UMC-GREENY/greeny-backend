package greeny.backend.domain.post.controller;

import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.service.PostService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "Create post API", description = "put your post info to create")
    @ResponseStatus(OK)
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE}, produces = APPLICATION_JSON_VALUE)
    public Response createPost(@Valid @RequestPart(name = "body") CreatePostRequestDto createPostRequestDto,
                               @RequestPart(name = "files", required = false)
                               List<MultipartFile> multipartFiles) {
        postService.creatPost(createPostRequestDto, multipartFiles);
        return success(SUCCESS_TO_CREATE_POST);
    }

    @Operation(summary = "Get post list api", description = "put page info what you want")
    @ResponseStatus(OK)
    @GetMapping
    public Response getPostList(@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_POST_LIST, postService.getPosts(pageable));
    }

}
