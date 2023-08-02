package greeny.backend.domain.store.service;

import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.dto.GetStoreInfoResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreInfos(String keyword, Pageable pageable) {

        if (StringUtils.hasText(keyword)) {
            return storeRepository.findStoresByNameContainingIgnoreCase(keyword, pageable)
                    .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
        }

        return storeRepository.findAll(pageable)
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, false));
    }

    public GetStoreInfoResponseDto getStoreInfo(Long storeId) {  // Store 상세 정보 가져오기
        return GetStoreInfoResponseDto.from(getStore(storeId));
    }

    public Store getStore(Long storeId) {  // Id 값을 통해 Store 객체 가져오기
        return storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);
    }
}
