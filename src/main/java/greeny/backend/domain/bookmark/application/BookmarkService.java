package greeny.backend.domain.bookmark.application;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.bookmark.entity.ProductBookmarkRepository;
import greeny.backend.domain.bookmark.entity.StoreBookmarkRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.presentation.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.application.ProductService;
import greeny.backend.domain.store.presentation.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.application.StoreService;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static greeny.backend.domain.Target.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final StoreBookmarkRepository storeBookmarkRepository;
    private final ProductBookmarkRepository productBookmarkRepository;
    private final StoreService storeService;
    private final ProductService productService;

    public List<StoreBookmark> getStoreBookmarks(Member liker) {
        return storeBookmarkRepository.findStoreBookmarksByLiker(liker);
    }

    public List<ProductBookmark> getProductBookmarks(Member liker){
        return productBookmarkRepository.findProductBookmarksByLiker(liker);
    }

    @Transactional(readOnly = true)
    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreBookmarksInfo(Pageable pageable , Member liker){
            return storeBookmarkRepository.findStoreBookmarksByLiker(pageable , liker)
                    .map(storeBookmark -> GetSimpleStoreInfosResponseDto.from(storeBookmark.getStore() , true));
    }

    @Transactional(readOnly = true)
    public Page<GetSimpleProductInfosResponseDto> getSimpleProductBookmarksInfo(Pageable pageable  , Member liker){
        return productBookmarkRepository.findProductBookmarksByLiker(pageable, liker)
                .map(productBookmark -> GetSimpleProductInfosResponseDto.from(productBookmark.getProduct(),true));
    }

    public void toggleStoreBookmark(String type, Long id, Member liker) {
        if(type.equals(STORE.toString())) {
            checkAndToggleStoreBookmarkBySituation(storeService.getStore(id), liker);
        }
        else if(type.equals(PRODUCT.toString())) {
            toggleProductBookmark(productService.getProduct(id), liker);
        }
        else {
            throw new TypeDoesntExistsException();
        }
    }

    private void checkAndToggleStoreBookmarkBySituation(Store store, Member liker) {
        Optional<StoreBookmark> storeBookmark = getOptionalStoreBookmark(store.getId(), liker.getId());

        if(storeBookmark.isPresent()) {
            storeBookmarkRepository.delete(storeBookmark.get());
        }
        else {
            storeBookmarkRepository.save(toEntity(store, liker));
        }
    }

    private void toggleProductBookmark(Product product, Member liker) {
        Optional<ProductBookmark> productBookmark = getOptionalProductBookmark(product.getId(), liker.getId());

        if(productBookmark.isPresent()) {
            productBookmarkRepository.delete(productBookmark.get());
        }
        else {
            productBookmarkRepository.save(toEntity(product, liker));
        }
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