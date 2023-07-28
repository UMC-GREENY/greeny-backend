package greeny.backend.domain.product.dto;

import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductInfoResponseDto {
    private Long id;
    private String name;
    private String imgUrl;
    private String storeName;
    private String storeUrl;
    private Integer price;
    private Integer deliveryFee;
    private Boolean isBookmarked;

    public static GetProductInfoResponseDto from(Product product, String storeName, String storeUrl, boolean isBookmarked){
        return GetProductInfoResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .imgUrl(product.getImageUrl())
                .storeName(storeName)
                .storeUrl(storeUrl)
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .isBookmarked(isBookmarked)
                .build();
    }
}
