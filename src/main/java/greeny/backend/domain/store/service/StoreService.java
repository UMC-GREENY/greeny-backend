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

    public Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    public List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos() {
        return storeRepository.findAll().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, store.getBookmarks().size(), store.getReviews().size()))
                .collect(Collectors.toList());
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId, List<StoreBookmark> myStoreBookmarks) {

        Store foundStore = getStore(storeId);

        for(StoreBookmark myStoreBookmark : myStoreBookmarks) {
            if(storeId.equals(myStoreBookmark.getStore().getId())) {
                return GetStoreInfoResponseDto.from(foundStore, true);
            }
        }

        return GetStoreInfoResponseDto.from(foundStore, false);
    }
}
