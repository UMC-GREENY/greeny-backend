package greeny.backend.domain.store.service;

import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.StoreNotFoundException;
import greeny.backend.exception.situation.TypeDoesntExistsException;
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

    public List<GetSimpleStoreInfosResponseDto> getNewOrBestSimpleStoreInfos(String type) {  // new or best store 목록 조회
        if(type.equals("new"))
            return getNewSimpleStoreInfos(type);
        else if(type.equals("best"))
            return getBestSimpleStoreInfos(type);
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

    private List<GetSimpleStoreInfosResponseDto> getNewSimpleStoreInfos(String type) {  // new store 목록 조회
        return storeRepository.findTop3ByOrderByIdDesc().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false, type))
                .collect(Collectors.toList());
    }

    private List<GetSimpleStoreInfosResponseDto> getBestSimpleStoreInfos(String type) {  // best store 목록 조회
        return storeRepository.findTop8ByOrderByBookmarksDesc().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false, type))
                .collect(Collectors.toList());
    }
}
