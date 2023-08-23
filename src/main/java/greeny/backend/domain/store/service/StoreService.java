package greeny.backend.domain.store.service;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.domain.store.repository.StoreSpecification;
import greeny.backend.exception.situation.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos(String keyword, String location, String category, Pageable pageable) {  // 모든 사용자에 대한 스토어 목록 조회
        return getStoresWithSpec(keyword, location, category, pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }

    // 인증된 사용자에 대한 스토어 목록 조회
    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfosWithAuthMember(String keyword, String location, String category, List<StoreBookmark> storeBookmarks, Pageable pageable) {
        return checkBookmarkedStore(
                getStoresWithSpec(keyword, location, category, pageable).getContent(),
                storeBookmarks,
                pageable
        );
    }
    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {  // Store 상세 정보 가져오기
        return GetStoreInfoResponseDto.from(getStore(storeId), false);
    }

    public GetStoreInfoResponseDto getStoreInfoWithAuthMember(Long storeId, Optional<StoreBookmark> optionalStoreBookmark) {

        if(optionalStoreBookmark.isPresent())
            return GetStoreInfoResponseDto.from(getStore(storeId), true);

        return GetStoreInfoResponseDto.from(getStore(storeId), false);
    }

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    private Page<Store> getStoresWithSpec(String keyword, String location, String category, Pageable pageable) {

        Specification<Store> spec = (root, query, criteriaBuilder) -> null;

        if(StringUtils.hasText(keyword))
            spec = spec.and(StoreSpecification.hasKeyword(keyword));

        if(StringUtils.hasText(location))
            spec = spec.and(StoreSpecification.hasLocation(location));

        if(StringUtils.hasText(category))
            spec = spec.and(StoreSpecification.equalCategory(category));

        return storeRepository.findAll(spec, pageable);
    }

    // 페이지네이션으로 불러온 스토어 목록과 인증된 사용자의 찜한 스토어 목록을 비교하여 찜 표시
    private Page<GetSimpleStoreInfosResponseDto> checkBookmarkedStore(List<Store> stores, List<StoreBookmark> storeBookmarks, Pageable pageable) {
        List<GetSimpleStoreInfosResponseDto> simpleStores = new ArrayList<>();

        for(Store store : stores) {

            boolean isBookmarked = false;

            for(StoreBookmark storeBookmark : storeBookmarks) {
                if(storeBookmark.getStore().getId().equals(store.getId())) {
                    isBookmarked = true;
                    simpleStores.add(GetSimpleStoreInfosResponseDto.from(store, isBookmarked));
                    storeBookmarks.remove(storeBookmark);
                    break;
                }
            }

            if(!isBookmarked) {
                simpleStores.add(GetSimpleStoreInfosResponseDto.from(store, isBookmarked));
            }
        }

        return new PageImpl<>(
                simpleStores,
                pageable,
                simpleStores.size()
        );
    }
}
