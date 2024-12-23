package greeny.backend.domain.community.presentation.controller;

import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.community.presentation.dto.WritePostRequestDto;
import greeny.backend.domain.community.application.PostService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts")
@Tag(name = "Post", description = "Post API Document")
@Slf4j
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @Operation(summary = "Write post API", description = "put your post info to write. you can skip files.")
    @ResponseStatus(OK)
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Response writePost(@Valid @RequestPart(name = "body(json)") WritePostRequestDto writePostRequestDto,
                               @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles) {
        postService.writePost(writePostRequestDto, multipartFiles, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_POST);
    }

    @Operation(summary = "Search simple post infos API",
            description = "put keyword and page info what you want. you can skip parameters. " +
                    " sort=id,desc : 일반 게시판 (최신순). " +
                    " sort=likes,desc&sort=hits,desc : 베스트 게시판 (좋아요순+조회수순).")
    @ResponseStatus(OK)
    @GetMapping("/search")
    public Response searchSimplePostInfos(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_SEARCH_POST_LIST, postService.searchSimplePostInfos(keyword, pageable));
    }

    @Operation(summary = "Get post info API", description = "put post id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getPostInfo(Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPostInfo(postId));

    }

    @Operation(summary = "Get post info with auth member API", description = "put post id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/auth")
    public Response getPostInfoWithAuthMember(Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPostInfoWithAuthMember(postId, memberService.getCurrentMember()));
    }

    @Operation(summary = "Delete post API", description = "put post id what you want to delete.")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deletePost(Long postId){
        postService.deletePost(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_POST);
    }
    @Operation(summary = "Edit post info API", description = "put post info what you want to edit.")
    @ResponseStatus(OK)
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Response editPostInfo(Long postId,
                                 @Valid @RequestPart(name = "body(json)") WritePostRequestDto editPostInfoRequestDto,
                                 @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles){
        postService.editPostInfo(postId, editPostInfoRequestDto, multipartFiles, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_EDIT_POST);
    }
}