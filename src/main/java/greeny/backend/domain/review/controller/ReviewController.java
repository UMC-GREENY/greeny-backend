package greeny.backend.domain.review.controller;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.review.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.service.ReviewService;
import greeny.backend.exception.situation.TypeDoesntExistsException;
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
@Tag(name = "Review", description = "Review API Document")
@RequestMapping(value = "/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    /* 리뷰 작성하기 API */
    @Operation(summary = "write review API", description="put review type & content and object type you want to write")
    @ResponseStatus(OK)
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Response writeReview(@RequestParam String type,
                                @RequestParam Long id,
                                @Valid @RequestPart(name="body") WriteReviewRequestDto writeReviewRequestDto,
                                @RequestPart(name="file", required = false)List<MultipartFile> multipartFiles) {
        if(type.equals("s")) {
            reviewService.writeStoreReview(id,writeReviewRequestDto,multipartFiles,memberService.getCurrentMember());
            return success(SUCCESS_TO_WRITE_STORE_REVIEW);
        }
        else if(type.equals("p")) {
            reviewService.writeProductReview(id,writeReviewRequestDto,multipartFiles,memberService.getCurrentMember());
            return success(SUCCESS_TO_WRITE_PRODUCT_REVIEW);
        }
        else throw new TypeDoesntExistsException();
    }

    /* 검색 OR getAllSimpleReviewInfos API */
    @Operation(summary = "get all simple review infos API", description="put review type and pageable object you want to get")
    @ResponseStatus(OK)
    @GetMapping("/all")
    public Response getAllSimpleReviewInfos(@RequestParam(required = false) String keyword,
                                            @RequestParam String type,
                                         @ParameterObject Pageable pageable) {
        return success(SUCCESS_TO_GET_ALL_REVIEW_LIST,reviewService.searchSimpleReviewInfos(keyword,type,pageable));
    }

    /* 스토어&제품 ID로 review list 불러오기 API */
    @Operation(summary = "get simple review infos API", description="put review type and pageable object you want to get")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleReviewInfos(@RequestParam String type,
                                  @RequestParam Long id,
                                  @ParameterObject Pageable pageable) {
        return success(SUCCESS_TO_GET_REVIEW_LIST,reviewService.getSimpleReviewInfos(type,id,pageable));
    }


    /* 상세리뷰 불러오기 : 인증X API */
    @Operation(summary = "Get review info API", description="put review type and reviewId you want to get")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getReviewInfo(@RequestParam String type,
                                    @RequestParam Long id) {
        if(type.equals("s")){
            return success(SUCCESS_TO_GET_STORE_REVIEW,reviewService.getStoreReviewInfo(id));
        }else if(type.equals("p")){
            return success(SUCCESS_TO_GET_PRODUCT_REVIEW,reviewService.getProductReviewInfo(id));
        }else {
            throw new TypeDoesntExistsException();
        }
    }
    /* 상세리뷰 불러오기 : 인증O API */
    @Operation(summary = "Get review info with Auth API", description="put review type and reviewId you want to get")
    @ResponseStatus(OK)
    @GetMapping("/auth")
    public Response getReviewInfoWithAuth(@RequestParam String type,
                                          @RequestParam Long id) {
        if(type.equals("s")){
            return success(SUCCESS_TO_GET_STORE_REVIEW,reviewService.getStoreReviewInfoWithAuth(id,memberService.getCurrentMember()));
        }else if(type.equals("p")){
            return success(SUCCESS_TO_GET_PRODUCT_REVIEW,reviewService.getProductReviewInfoWithAuth(id,memberService.getCurrentMember()));
        }else {
            throw new TypeDoesntExistsException();
        }
    }

    /* 리뷰 삭제 API */
    @Operation(summary = "Delete review API", description = "put review type and object id you want to delete")
    @ResponseStatus(OK)
    @DeleteMapping()
    public Response deleteReview(@RequestParam String type,
                                 @RequestParam Long id) {
        Member currentMember = memberService.getCurrentMember();
        if(type.equals("s")) {
            reviewService.deleteStoreReview(id,currentMember);
            return success(SUCCESS_TO_DELETE_STORE_REVIEW);
        }
        else if(type.equals("p")) {
            reviewService.deleteProductReview(id,currentMember);
            return success(SUCCESS_TO_DELETE_PRODUCT_REVIEW);
        } else throw new TypeDoesntExistsException();
    }
}
