package greeny.backend.domain.review.dto;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteReviewRequestDto {
    @NotNull(message = "별점을 입력해주세요.")
    @Schema(description = "별점", defaultValue = "4")
    private Integer star;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 300, message = "300자 이하로 입력해주세요.")
    @Schema(description = "리뷰 내용", defaultValue = "default review content")
    private String content;

    public StoreReview toStoreReviewEntity(Member member, Store store) {
        return StoreReview.builder()
                .reviewer(member)
                .store(store)
                .content(this.content)
                .star(this.star)
                .build();
    }

    public ProductReview toProductReviewEntity(Member member, Product product) {
        return ProductReview.builder()
                .reviewer(member)
                .product(product)
                .content(this.content)
                .star(this.star)
                .build();
    }
}
