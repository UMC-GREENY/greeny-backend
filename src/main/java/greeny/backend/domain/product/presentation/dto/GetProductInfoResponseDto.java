package greeny.backend.domain.product.presentation.dto;

import greeny.backend.domain.product.entity.Product;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetProductInfoResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String storeName;
    private String webUrl;
    private Integer price;
    private Integer deliveryFee;
    private String contentUrl;
    private String phone;
    private Boolean isBookmarked;

    public static GetProductInfoResponseDto from(Product product, boolean isBookmarked) {
        return GetProductInfoResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .storeName(product.getStore().getName())
                .webUrl(product.getStore().getWebUrl())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .contentUrl(product.getContentUrl())
                .phone(product.getStore().getPhone())
                .isBookmarked(isBookmarked)
                .build();
    }
}