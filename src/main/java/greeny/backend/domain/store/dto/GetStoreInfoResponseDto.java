package greeny.backend.domain.store.dto;

import greeny.backend.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStoreInfoResponseDto {  // 스토어 상세 정보
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
