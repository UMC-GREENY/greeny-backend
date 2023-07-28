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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    public List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos() {  // Store 목록 가져오기
        return storeRepository.findStoresWithBookmarksAndReviews().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, store.getBookmarks().size(), store.getReviews().size()))
                .collect(Collectors.toList());  // List 에서 하나의 store 마다 GetSimpleStoreInfosResponseDto 객체로 변환 후 List 에 담아서 반환
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId, List<StoreBookmark> myStoreBookmarks) {  // Store 상세 정보 가져오기

        Store foundStore = getStore(storeId);

        for(StoreBookmark myStoreBookmark : myStoreBookmarks) {  // 현재 사용자의 찜한 스토어 목록과 현재 스토어 id 값 비교를 통해 찜한 여부 판단
            if(storeId.equals(myStoreBookmark.getStore().getId())) {
                return GetStoreInfoResponseDto.from(foundStore, true);
            }
        }

        return GetStoreInfoResponseDto.from(foundStore, false);
    }
}
