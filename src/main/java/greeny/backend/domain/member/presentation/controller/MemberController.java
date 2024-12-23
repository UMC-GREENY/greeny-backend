package greeny.backend.domain.member.presentation.controller;

import greeny.backend.domain.bookmark.application.BookmarkService;
import greeny.backend.domain.community.application.PostService;
import greeny.backend.domain.member.presentation.dto.CancelBookmarkRequestDto;
import greeny.backend.domain.member.presentation.dto.EditMemberInfoRequestDto;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.review.application.ReviewService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
@Tag(name = "Member", description = "Member API Document")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final BookmarkService bookmarkService;

    @Operation(summary = "Edit member info API", description = "put info what you want to change")
    @ResponseStatus(OK)
    @PatchMapping()
    public Response editMemberInfo(@Valid @RequestBody EditMemberInfoRequestDto editMemberRequestDto) {
        memberService.editMemberInfo(editMemberRequestDto);
        return success(SUCCESS_TO_EDIT_MEMBER_PASSWORD);
    }

    @Operation(summary = "Get member info API")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getMemberInfo() {
        return success(SUCCESS_TO_GET_MEMBER_INFO, memberService.getMemberInfo());
    }

    @Operation(summary = "Delete member API", description = "this is to delete member")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deleteMember() {
        memberService.deleteMember();
        return success(SUCCESS_TO_DELETE_MEMBER);
    }

    @Operation(summary = "Delete store or product bookmark API", description = "put your store or product id what you want to delete.")
    @ResponseStatus(OK)
    @DeleteMapping("/bookmark")
    public Response cancelBookmark(String type, @RequestBody CancelBookmarkRequestDto cancelBookmarkRequestDto) {
        memberService.cancelBookmark(type, cancelBookmarkRequestDto);
        return success(SUCCESS_TO_CANCEL_BOOKMARK);
    }

    @Operation(summary = "Get my post simple infos API", description = "put page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/post")
    public Response getMySimplePostInfos(@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_POST_LIST, postService.getMySimplePostInfos(pageable, memberService.getCurrentMember()));
    }

    @Operation(summary = "Get my review list api", description = "put page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/review")
    public Response getMyReviewList(String type,@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_REVIEW_LIST, reviewService.getMemberReviewList(type,pageable, memberService.getCurrentMember()));
    }

    @Operation(summary = "Get my store-bookmark list api")
    @ResponseStatus(OK)
    @GetMapping("/simple/store-bookmark")
    public Response getSimpleStoreBookmarkInfos(@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_STORE_BOOKMARK , bookmarkService.getSimpleStoreBookmarksInfo(pageable , memberService.getCurrentMember()));
    }

    @Operation(summary = "Get my product-bookmark list api")
    @ResponseStatus(OK)
    @GetMapping("/simple/product-bookmark")
    public Response getSimpleProductBookmarkInfos(@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_PRODUCT_BOOKMARK, bookmarkService.getSimpleProductBookmarksInfo(pageable , memberService.getCurrentMember()));
    }
}