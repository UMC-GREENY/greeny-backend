package greeny.backend.domain.bookmark.service;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.bookmark.repository.ProductBookmarkRepository;
import greeny.backend.domain.bookmark.repository.StoreBookmarkRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.service.ProductService;
import greeny.backend.domain.store.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.service.StoreService;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {  // Controller -> Service 의존성을 유지하려고 했으나 해결되지 않는다면 Service -> Service 의존 설계도 가능

    private final StoreBookmarkRepository storeBookmarkRepository;
    private final ProductBookmarkRepository productBookmarkRepository;
    private final StoreService storeService;
    private final ProductService productService;

    public List<StoreBookmark> getStoreBookmarks(Member liker) {  // 현재 사용자의 찜한 스토어 목록 가져오기
        return storeBookmarkRepository.findStoreBookmarksByLiker(liker);
    }

    public List<ProductBookmark> getProductBookmarks(Member liker){
        return productBookmarkRepository.findProductBookmarksByLiker(liker);
    }

    //찜한 스토어 목록 불러오기
    @Transactional(readOnly = true)
    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreBookmarksInfo(Pageable pageable , Member liker){
            return storeBookmarkRepository.findStoreBookmarksByLiker(pageable , liker)
                    .map(storeBookmark -> GetSimpleStoreInfosResponseDto.from(storeBookmark.getStore() , true));
    }

    //찜한 제품 목록 불러오기
    @Transactional(readOnly = true)
    public Page<GetSimpleProductInfosResponseDto> getSimpleProductBookmarksInfo(Pageable pageable  , Member liker){
        
        return productBookmarkRepository.findProductBookmarksByLiker(pageable, liker)
                .map(productBookmark -> GetSimpleProductInfosResponseDto.from(productBookmark.getProduct(),true));
    }

    public void toggleStoreBookmark(String type, Long id, Member liker) {  // 타입에 따라 찜하기 or 취소
        if(type.equals("store")) {  // 스토어 찜하기
            checkAndToggleStoreBookmarkBySituation(storeService.getStore(id), liker);
        } else if(type.equals("product")) {  // 제품 찜하기
            toggleProductBookmark(productService.getProduct(id), liker);
        } else {
            throw new TypeDoesntExistsException();
        }
    }

    private void checkAndToggleStoreBookmarkBySituation(Store store, Member liker) {  // 찜한 정보 DB에 저장 or 취소 시 DB 에서 삭제
        Optional<StoreBookmark> storeBookmark = getOptionalStoreBookmark(store.getId(), liker.getId());

        if(storeBookmark.isPresent())
            storeBookmarkRepository.delete(storeBookmark.get());
        else
            storeBookmarkRepository.save(toEntity(store, liker));
    }

    private void toggleProductBookmark(Product product, Member liker) {  // 찜한 정보 DB에 저장 or 취소 시 DB 에서 삭제
        Optional<ProductBookmark> productBookmark = getOptionalProductBookmark(product.getId(), liker.getId());

        if(productBookmark.isPresent())
            productBookmarkRepository.delete(productBookmark.get());
        else
            productBookmarkRepository.save(toEntity(product, liker));
    }

    private StoreBookmark toEntity(Store store, Member liker) {
        return StoreBookmark.builder()
                .store(store)
                .liker(liker)
                .build();
    }

    private ProductBookmark toEntity(Product product, Member liker) {
        return ProductBookmark.builder()
                .product(product)
                .liker(liker)
                .build();
    }

    public Optional<StoreBookmark> getOptionalStoreBookmark(Long storeId, Long likerId) {
        return storeBookmarkRepository.findByStoreIdAndLikerId(storeId, likerId);
    }

    public Optional<ProductBookmark> getOptionalProductBookmark(Long productId, Long likerId) {
        return productBookmarkRepository.findByProductIdAndLikerId(productId, likerId);
    }
}
