package greeny.backend.domain.review.controller;

import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.review.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.service.ReviewService;
import greeny.backend.exception.situation.WrongTypeException;
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
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API Document")
@RequestMapping(value = "/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

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
        else throw new WrongTypeException();
    }

    @GetMapping(params={"type","pageable"})
    public Response getReviewList(@RequestParam String type,
                                  @ParameterObject Pageable pageable) {
        return success(SUCCESS_TO_GET_REVIEW_LIST);
    }

    @GetMapping(params = {"type","id"})
    public Response getReviewDetail(@RequestParam String type,
                                    @RequestParam Long id) {
        return success(SUCCESS_TO_GET_PRODUCT_REVIEW);
    }

    @Operation(summary = "delete review api", description = "put review type and id you want to delete")
    @DeleteMapping()
    public Response deleteReview(@RequestParam String type,
                                 @RequestParam Long id) {

        if(type=="s") {
            reviewService.deleteStoreReview(id);
            return success(SUCCESS_TO_WRITE_STORE_REVIEW);
        }
        else {
            reviewService.deleteProductReview(id);
            return success(SUCCESS_TO_WRITE_PRODUCT_REVIEW);
        }
    }

}
