package greeny.backend.domain.store.service;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.community.dto.GetSimplePostInfosResponseDto;
import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos(String keyword, String location, String category, Pageable pageable) {  // 모든 사용자에 대한 스토어 목록 조회

        if (StringUtils.hasText(keyword) && !StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return getStoresWithKeyword(keyword, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(!StringUtils.hasText(keyword) && StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return getStoresWithLocation(location, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(!StringUtils.hasText(keyword) && !StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return getStoresWithCategory(category, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(StringUtils.hasText(keyword) && StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return getStoresWithKeywordAndLocation(keyword, location, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(StringUtils.hasText(keyword) && !StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return getStoresWithKeywordAndCategory(keyword, category, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(!StringUtils.hasText(keyword) && StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return getStoresWithLocationAndCategory(location, category, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        if(StringUtils.hasText(keyword) && StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return getStoresWithKeywordAndLocationAndCategory(keyword, location, category, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        return getStores(pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }

    // 인증된 사용자에 대한 스토어 목록 조회
    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfoWithAuthMember(String keyword, String location, String category, List<StoreBookmark> storeBookmarks, Pageable pageable) {

        if(StringUtils.hasText(keyword)) {
            return checkBookmarkedStore(
                    storeRepository.findStoresByNameContainingIgnoreCase(keyword, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if (StringUtils.hasText(keyword) && !StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithKeyword(keyword, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(!StringUtils.hasText(keyword) && StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithLocation(location, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(!StringUtils.hasText(keyword) && !StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithCategory(category, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(StringUtils.hasText(keyword) && StringUtils.hasText(location) && !StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithKeywordAndLocation(keyword, location, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(StringUtils.hasText(keyword) && !StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithKeywordAndCategory(keyword, category, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(!StringUtils.hasText(keyword) && StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithLocationAndCategory(location, category, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        if(StringUtils.hasText(keyword) && StringUtils.hasText(location) && StringUtils.hasText(category)) {
            return checkBookmarkedStore(
                    getStoresWithKeywordAndLocationAndCategory(keyword, location, category, pageable).getContent(),
                    storeBookmarks,
                    pageable
            );
        }

        return checkBookmarkedStore(
                getStores(pageable).getContent(),
                storeBookmarks,
                pageable
        );
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {  // Store 상세 정보 가져오기
        return GetStoreInfoResponseDto.from(getStore(storeId));
    }

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    private Page<Store> getStoresWithKeyword(String keyword, Pageable pageable) {
        return storeRepository.findStoresByNameContainingIgnoreCase(keyword, pageable);
    }

    private Page<Store> getStoresWithLocation(String location, Pageable pageable) {
        return storeRepository.findStoresByLocationContainingIgnoreCase(location, pageable);
    }

    private Page<Store> getStoresWithCategory(String category, Pageable pageable) {
        return storeRepository.findStoresByCategoryContainingIgnoreCase(category, pageable);
    }

    private Page<Store> getStoresWithKeywordAndLocation(String keyword, String location, Pageable pageable) {
        return storeRepository.findStoresByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(keyword, location, pageable);
    }

    private Page<Store> getStoresWithKeywordAndCategory(String keyword, String category, Pageable pageable) {
        return storeRepository.findStoresByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(keyword, category, pageable);
    }

    private Page<Store> getStoresWithLocationAndCategory(String location, String category, Pageable pageable) {
        return storeRepository.findStoresByLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(location, category, pageable);
    }

    private Page<Store> getStoresWithKeywordAndLocationAndCategory(String keyword, String location, String category, Pageable pageable) {
        return storeRepository.findStoresByNameContainingIgnoreCaseAndLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(keyword, location, category, pageable);
    }

    private Page<Store> getStores(Pageable pageable) {
        return storeRepository.findAll(pageable);
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
