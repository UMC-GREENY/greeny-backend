package greeny.backend.domain.review.presentation.dto;

import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetReviewListResponseDto {
    private Long id;
    private String createdAt;
    private String writerEmail;
    private int star;
    private String content;
    private boolean existsFile;
    private String type;
    private Long idByType;

    public static GetReviewListResponseDto from(StoreReview review) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getStoreReviewImages().isEmpty())
                .build();
    }

    public static GetReviewListResponseDto toDetailStoreDto(StoreReview review, String type, Long idByType) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getStoreReviewImages().isEmpty())
                .type(type)
                .idByType(idByType)
                .build();
    }

    public static GetReviewListResponseDto from(ProductReview review) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getProductReviewImages().isEmpty())
                .build();
    }

    public static GetReviewListResponseDto toDetailProductDto(ProductReview review, String type, Long idByType) {
        return GetReviewListResponseDto.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .writerEmail(review.getReviewer().getEmail())
                .star(review.getStar())
                .content(review.getContent())
                .existsFile(!review.getProductReviewImages().isEmpty())
                .type(type)
                .idByType(idByType)
                .build();
    }
}