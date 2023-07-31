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
public class GetSimpleStoreInfosResponseDto {  // 스토어 목록에 보여주는 정보
    private Long id;
    private String category;
    private String name;
    private String imageUrl;
    private String location;
    private Boolean isBookmarked;  // 현재 사용자가 찜을 했는지 여부
    private String type;

    public static GetSimpleStoreInfosResponseDto from(Store store, boolean isBookmarked, String type) {
        return GetSimpleStoreInfosResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory().getName())
                .name(store.getName())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation().substring(0, 2))  // 지역 별 필터링을 위한 지역 키워드 추출 (Ex. 서울, 경기, 인천, 충남, 충북 등)
                .isBookmarked(isBookmarked)
                .type(type)
                .build();
    }
}
