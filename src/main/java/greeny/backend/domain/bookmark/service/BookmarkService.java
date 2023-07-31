package greeny.backend.domain.bookmark.service;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.bookmark.repository.ProductBookmarkRepository;
import greeny.backend.domain.bookmark.repository.StoreBookmarkRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.service.ProductService;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.service.StoreService;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void toggleStoreBookmark(String type, Long id, Member liker) {  // 타입에 따라 찜하기 or 취소

        if(type.equals("s")) {  // 스토어 찜하기
            toggleStoreBookmark(storeService.getStore(id), liker);
        } else if(type.equals("p")) {  // 제품 찜하기
            toggleProductBookmark(productService.getProduct(id), liker);
        } else {
            throw new TypeDoesntExistsException();
        }
    }

    private void toggleStoreBookmark(Store store, Member liker) {  // 찜한 정보 DB에 저장 or 취소 시 DB 에서 삭제
        List<StoreBookmark> foundStoreBookmarks = storeBookmarkRepository.findStoreBookmarksByLiker(liker);
        Long storeIdToCheck = store.getId();

        for(StoreBookmark storeBookmark : foundStoreBookmarks) {  // 이미 찜이 된 스토어라면 찜 취소 -> DB 에서 삭제
            if(storeIdToCheck.equals(storeBookmark.getStore().getId())) {
                storeBookmarkRepository.delete(storeBookmark);
                return;
            }
        }

        storeBookmarkRepository.save(toEntity(store, liker));  // 찜이 안된 상태라면 찜 하기 -> DB 에 저장
    }

    private void toggleProductBookmark(Product product, Member liker) {  // 찜한 정보 DB에 저장 or 취소 시 DB 에서 삭제
        List<ProductBookmark> foundProductBookmarks = productBookmarkRepository.findProductBookmarksByLiker(liker);
        Long productIdToCheck = product.getId();

        for(ProductBookmark productBookmark : foundProductBookmarks) {
            if(productIdToCheck.equals(productBookmark.getProduct().getId())) {
                productBookmarkRepository.delete(productBookmark);
                return;
            }
        }

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
}
