package greeny.backend.domain.store.presentation.dto;

import greeny.backend.domain.store.entity.Store;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetStoreInfoResponseDto {
    private Long id;
    private String category;
    private String name;
    private String webUrl;
    private String imageUrl;
    private String location;
    private String phone;
    private String runningTime;
    private Boolean isBookmarked;

    public static GetStoreInfoResponseDto from(Store store, boolean isBookmarked) {
        return GetStoreInfoResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory())
                .name(store.getName())
                .webUrl(store.getWebUrl())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation())
                .phone(store.getPhone())
                .runningTime(store.getRunningTime())
                .isBookmarked(isBookmarked)
                .build();
    }
}