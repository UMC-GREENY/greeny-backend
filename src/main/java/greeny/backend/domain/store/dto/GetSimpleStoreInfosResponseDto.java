package greeny.backend.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Integer bookmarks;  // 찜한 사람들의 수
    private Integer reviews;  // 리뷰 수
    private Boolean isBookmarked;  // 현재 사용자가 찜을 했는지 여부

    public static GetSimpleStoreInfosResponseDto from(Store store, int bookmarks, int reviews, boolean isBookmarked) {
        return GetSimpleStoreInfosResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory().getName())
                .name(store.getName())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation())
                .bookmarks(bookmarks)
                .reviews(reviews)
                .isBookmarked(isBookmarked)
                .build();
    }
    public static GetSimpleStoreInfosResponseDto from(Store store, int bookmarks) {
        return GetSimpleStoreInfosResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory().getName())
                .name(store.getName())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation())
                .bookmarks(bookmarks)
                .reviews(0)
                .build();
    }
}
