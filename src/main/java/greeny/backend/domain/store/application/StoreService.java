package greeny.backend.domain.store.application;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.store.presentation.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.presentation.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.entity.StoreRepository;
import greeny.backend.domain.store.entity.StoreSpecification;
import greeny.backend.exception.situation.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos(String keyword, String location, String category, Pageable pageable) {
        return getStoresBySpec(keyword, location, category, pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }

    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfosWithAuthMember(String keyword, String location, String category, List<StoreBookmark> storeBookmarks, Pageable pageable) {
        return checkBookmarkedStore(
                getStoresBySpec(keyword, location, category, pageable).getContent(),
                storeBookmarks,
                pageable
        );
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {
        return GetStoreInfoResponseDto.from(getStore(storeId), false);
    }

    public GetStoreInfoResponseDto getStoreInfoWithAuthMember(Long storeId, Optional<StoreBookmark> optionalStoreBookmark) {

        if(optionalStoreBookmark.isPresent())
            return GetStoreInfoResponseDto.from(getStore(storeId), true);

        return GetStoreInfoResponseDto.from(getStore(storeId), false);
    }

    public Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    private Page<Store> getStoresBySpec(String keyword, String location, String category, Pageable pageable) {
        return storeRepository.findAll(
                StoreSpecification.create((root, query, criteriaBuilder) -> null, keyword, location, category), pageable
        );
    }

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