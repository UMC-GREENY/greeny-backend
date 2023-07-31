package greeny.backend.domain.store.service;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.StoreNotFoundException;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public List<GetSimpleStoreInfosResponseDto> getNewOrBestSimpleStoreInfos(String type) {  // 모든 사용자의 new or best store 목록 조회
        if(type.equals("new"))
            return getNewSimpleStoreInfos();
        else if(type.equals("best"))
            return getBestSimpleStoreInfos();
        else
            throw new TypeDoesntExistsException();
    }

    public List<GetSimpleStoreInfosResponseDto> getNewOrBestSimpleStoreInfoWithAuthMember(String type, List<StoreBookmark> foundStoreBookmarks) {  // 인증된 사용자의 new or best store 목록 조회
        if(type.equals("new"))
            return getNewSimpleStoreInfosWithAuthMember(foundStoreBookmarks);  // 현재 사용자의 찜한 여부와 함께 new store 목록 조회
        else if(type.equals("best"))
            return getBestSimpleStoreInfosWithAuthMember(foundStoreBookmarks);  // 현재 사용자의 찜한 여부와 함께 best store 목록 조회
        else
            throw new TypeDoesntExistsException();
    }

    public Page<GetSimpleStoreInfosResponseDto> getSortedSimpleStoreInfos(String type, Pageable pageable) {  // 조건에 따라 정렬하여 store 목록 조회
        if(type.equals("bookmark"))  // 인기순 정렬일 경우
            return getSortedSimpleStoreInfosByBookmark(pageable);
        else if(type.equals("review"))  // 후기순 정렬일 경우
            return getSortedSimpleStoreInfosByReview(pageable);
        else
            throw new TypeDoesntExistsException();
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {  // Store 상세 정보 가져오기
        return GetStoreInfoResponseDto.from(getStore(storeId));
    }

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }

    private List<GetSimpleStoreInfosResponseDto> getNewSimpleStoreInfos() {  // New store 목록 조회
        return storeRepository.findTop3ByOrderByIdDesc().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false))
                .collect(Collectors.toList());
    }
    private List<GetSimpleStoreInfosResponseDto> getNewSimpleStoreInfosWithAuthMember(List<StoreBookmark> storeBookmarks) {  // 현재 사용자의 찜한 여부와 함께 new store 목록 조회
        return getSimpleStoreInfosWithBookmark(storeRepository.findTop3ByOrderByIdDesc(), storeBookmarks);
    }
    private List<GetSimpleStoreInfosResponseDto> getBestSimpleStoreInfos() {  // Best store 목록 조회
        return storeRepository.findTop8ByOrderByBookmarksDesc().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false))
                .collect(Collectors.toList());
    }
    private List<GetSimpleStoreInfosResponseDto> getBestSimpleStoreInfosWithAuthMember(List<StoreBookmark> storeBookmarks) {  // 현재 사용자의 찜한 여부와 함께 best store 목록 조회
        return getSimpleStoreInfosWithBookmark(storeRepository.findTop8ByOrderByBookmarksDesc(), storeBookmarks);
    }
    private Page<GetSimpleStoreInfosResponseDto> getSortedSimpleStoreInfosByBookmark(Pageable pageable) {  // 인기순으로 정렬한 store 목록 조회
        return storeRepository.findStoresByOrderByBookmarksDesc(pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }
    private Page<GetSimpleStoreInfosResponseDto> getSortedSimpleStoreInfosByReview(Pageable pageable) {
        return storeRepository.findStoresByOrderByReviewsDesc(pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }
    private List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfosWithBookmark(List<Store> stores, List<StoreBookmark> storeBookmarks) {  // Store 목록에서 현재 사용자의 찜 여부 찾기
        List<GetSimpleStoreInfosResponseDto> simpleStores = new ArrayList<>();

        for(Store store : stores) {

            boolean isBookmarked = false;

            for(StoreBookmark storeBookmark : storeBookmarks) {
                if(storeBookmark.getStore().getId().equals(store.getId())) {  // 현재 사용자의 store 찜 목록에서 일치하는 store 찾았을 경우
                    isBookmarked = true;
                    simpleStores.add(GetSimpleStoreInfosResponseDto.from(store, isBookmarked));
                    break;
                }
            }

            if(!isBookmarked) {  // 일치하지 않을 경우
                simpleStores.add(GetSimpleStoreInfosResponseDto.from(store, isBookmarked));
            }
        }

        return simpleStores;
    }
}
