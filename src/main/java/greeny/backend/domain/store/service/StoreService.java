package greeny.backend.domain.store.service;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos() {
        return storeRepository.findStoresWithBookmarksAndReviews().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, store.getBookmarks().size(), store.getReviews().size(), false))
                .collect(Collectors.toList());
    }

    public List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfosWithAuthMember(List<StoreBookmark> storeBookmarks) {  // 인증된 사용자의 Store 목록 가져오기

        List<GetSimpleStoreInfosResponseDto> simpleStoreInfos = new ArrayList<>();
        List<Store> foundStores = storeRepository.findStoresWithBookmarksAndReviews();

        for(Store store : foundStores) {

            boolean isBookmarked = false;  // 사용자가 찜한 스토어인지 판단

            for(StoreBookmark storeBookmark : storeBookmarks) {
                if(storeBookmark.getStore().getId().equals(store.getId())) {  // 사용자가 찜한 스토어의 경우
                    isBookmarked = true;  // true 로 변경
                    simpleStoreInfos.add(
                            GetSimpleStoreInfosResponseDto.from(store, store.getBookmarks().size(), store.getReviews().size(), isBookmarked)
                    );
                    break;
                }
            }

            if(!isBookmarked) {  // 사용자가 찜한 스토어가 아닐 경우
                simpleStoreInfos.add(
                        GetSimpleStoreInfosResponseDto.from(store, store.getBookmarks().size(), store.getReviews().size(), isBookmarked)
                );
            }
        }

        return simpleStoreInfos;
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {  // Store 상세 정보 가져오기
        return GetStoreInfoResponseDto.from(getStore(storeId));
    }

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }
}
