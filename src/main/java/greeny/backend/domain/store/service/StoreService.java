package greeny.backend.domain.store.service;

import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public List<GetSimpleStoreInfosResponseDto> getSimpleStoreInfosWithReview() {
        return storeRepository.findAll().stream()
                .map(store -> GetSimpleStoreInfosResponseDto.from(store, store.getStoreBookmarks().size(), store.getStoreReviews().size()))
                .collect(Collectors.toList());
    }


}
