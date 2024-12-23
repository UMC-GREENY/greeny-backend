package greeny.backend.domain.store.presentation.dto;

import greeny.backend.domain.store.entity.Store;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetSimpleStoreInfosResponseDto {
    private Long id;
    private String category;
    private String name;
    private String imageUrl;
    private String location;
    private Boolean isBookmarked;

    public static GetSimpleStoreInfosResponseDto from(Store store, boolean isBookmarked) {
        return GetSimpleStoreInfosResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory())
                .name(store.getName())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation().substring(0, 2))
                .isBookmarked(isBookmarked)
                .build();
    }
}