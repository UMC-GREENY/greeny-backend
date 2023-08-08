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
public class GetProductInfoResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String storeName;
    private String webUrl;
    private Integer price;
    private Integer deliveryFee;

    public static GetProductInfoResponseDto from(Product product){
        return GetProductInfoResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .storeName(product.getStore().getName())
                .webUrl(product.getStore().getWebUrl())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .build();
    }
}
