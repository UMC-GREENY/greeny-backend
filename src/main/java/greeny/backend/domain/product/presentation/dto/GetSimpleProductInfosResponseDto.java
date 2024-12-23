package greeny.backend.domain.product.presentation.dto;

import greeny.backend.domain.product.entity.Product;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetSimpleProductInfosResponseDto {
    private Long id;
    private String productName;
    private String imageUrl;
    private String storeName;
    private Integer price;
    private boolean isBookmarked;

    public static GetSimpleProductInfosResponseDto from (Product product,boolean isBookmarked){
        return GetSimpleProductInfosResponseDto.builder()
                .id(product.getId())
                .productName(product.getName())
                .imageUrl(product.getImageUrl())
                .storeName(product.getStore().getName())
                .price(product.getPrice())
                .isBookmarked(isBookmarked)
                .build();
    }
}