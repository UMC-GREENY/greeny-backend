package greeny.backend.domain.review.dto;

import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetReviewListResponseDto {
    private Long id;
    private String createdAt;
    private String writerEmail;
    private int star;
    private String content;
    private boolean existsFile;

    public static GetReviewListResponseDto toStoreReviewDTO(StoreReview review) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getStoreReviewImages().isEmpty())
                .build();
    }
    public static GetReviewListResponseDto toProductReviewDTO(ProductReview review) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getProductReviewImages().isEmpty())
                .build();
    }

}
