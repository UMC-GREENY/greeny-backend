package greeny.backend.domain.bookmark.service;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.bookmark.repository.ProductBookmarkRepository;
import greeny.backend.domain.bookmark.repository.StoreBookmarkRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final StoreBookmarkRepository storeBookmarkRepository;
    private final ProductBookmarkRepository productBookmarkRepository;

    public List<StoreBookmark> getStoreBookmarkInfos(Member liker) {  // 현재 사용자의 찜한 스토어 목록 가져오기
        return storeBookmarkRepository.findStoreBookmarksByLiker(liker);
    }

    public void toggleStoreBookmark(String type, Store store, Product product, Member liker) {  // 타입에 따라 찜하기 or 취소

        if(type.equals("s")) {  // 스토어 찜하기
            toggleStoreBookmark(store, liker);
        } else if(type.equals("p")) {  // 제품 찜하기
            toggleProductBookmark(product, liker);
        } else {
            throw new TypeDoesntExistsException();
        }
    }

    private void toggleStoreBookmark(Store store, Member liker) {  // 찜한 정보 DB에 저장 or 취소 시 DB 에서 삭제
        List<StoreBookmark> foundStoreBookmarks = storeBookmarkRepository.findStoreBookmarksByLiker(liker);
        Long storeIdToCheck = store.getId();

        for(StoreBookmark storeBookmark : foundStoreBookmarks) {
            if(storeIdToCheck.equals(storeBookmark.getStore().getId())) {
                storeBookmarkRepository.delete(storeBookmark);
                return;
            }
        }

        storeBookmarkRepository.save(toEntity(store, liker));
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
