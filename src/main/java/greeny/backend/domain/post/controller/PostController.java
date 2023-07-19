package greeny.backend.domain.post.controller;

import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.service.PostService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static greeny.backend.domain.SuccessMessage.SUCCESS_TO_CREATE_POST;
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

}
