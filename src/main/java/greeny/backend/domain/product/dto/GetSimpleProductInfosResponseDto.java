package greeny.backend.domain.product.dto;

import greeny.backend.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetSimpleProductInfosResponseDto {
    private Long id;
    private String productName;
    private String imageUrl;
    private String storeName;
    private Integer price;
    private Integer bookmarks;
    private Integer reviews;
    private Boolean isBookmarked;

    public static GetSimpleProductInfosResponseDto from (Product product, String storeName, int bookmarks, int reviews, boolean isBookmarked){
        return GetSimpleProductInfosResponseDto.builder()
                .id(product.getId())
                .productName(product.getName())
                .imageUrl(product.getImageUrl())
                .storeName(storeName)
                .price(product.getPrice())
                .bookmarks(bookmarks)
                .reviews(reviews)
                .isBookmarked(isBookmarked)
                .build();
    }
}
