package greeny.backend.domain.member.controller;


import greeny.backend.domain.board.service.PostService;
import greeny.backend.domain.member.dto.member.CancelBookmarkRequestDto;
import greeny.backend.domain.member.dto.member.EditMemberInfoRequestDto;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.review.service.ReviewService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
@Tag(name = "Member", description = "Member API Document")
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final ReviewService reviewService;

    // 사용자 비밀번호 변경 API
    @Operation(summary = "Edit member info API", description = "put info what you want to change")
    @ResponseStatus(OK)
    @PatchMapping()
    public Response editMemberInfo(@Valid @RequestBody EditMemberInfoRequestDto editMemberRequestDto) {
        memberService.editMemberInfo(editMemberRequestDto);
        return success(SUCCESS_TO_EDIT_MEMBER_PASSWORD);
    }

    // 사용자 정보 불러오기 API
    @Operation(summary = "Get member info API")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getMemberInfo() {
        //회원 정보를 조회 할 수 있게 입력 받은 회원의 정보를 주는 컨트롤러
        return success(SUCCESS_TO_GET_MEMBER_INFO, memberService.getMemberInfo());
    }

    // 회원 탈퇴 API
    @Operation(summary = "Delete member API", description = "this is to delete member")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deleteMember() {
        //회원 탈퇴를 실행해주는 컨트롤러
        memberService.deleteMember();
        return success(SUCCESS_TO_DELETE_MEMBER);
    }

    // 사용자가 찜한 store or product 목록에서 삭제 API
    @Operation(summary = "Delete store or product bookmark API", description = "put your store or product id what you want to delete.")
    @ResponseStatus(OK)
    @DeleteMapping("/bookmark")
    public Response cancelBookmark(String type, @RequestBody CancelBookmarkRequestDto cancelBookmarkRequestDto) {
        memberService.cancelBookmark(type, cancelBookmarkRequestDto);
        return success(SUCCESS_TO_CANCEL_BOOKMARK);
    }

    // 사용자 게시글 작성 목록 가져오기 API
    @Operation(summary = "Get my post list api", description = "put page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/post")
    public Response getMyPostList(@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_POST_LIST, postService.getMemberPostList(pageable, memberService.getCurrentMember()));
    }

    @Operation(summary = "Get my review list api", description = "put page info what you want. you can skip parameters.")
    @ResponseStatus(OK)
    @GetMapping("/review")
    public Response getMyReviewList(String type,@ParameterObject Pageable pageable) {
        return Response.success(SUCCESS_TO_GET_REVIEW_LIST, reviewService.getMemberReviewList(type,pageable, memberService.getCurrentMember()));
    }
}
