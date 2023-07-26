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

    public List<StoreBookmark> getMyStoreBookmarkInfos(Member liker) {  // 현재 사용자의 찜한 스토어 목록 가져오기
        return storeBookmarkRepository.findStoreBookmarksByLiker(liker);
    }

    public void addBookmark(String type, Store store, Product product, Member liker) {  // 타입에 따라 찜하기

        if(type.equals("s")) {  // 스토어 찜하기
            addStoreBookmark(store, liker);
        } else if(type.equals("p")) {  // 제품 찜하기
            addProductBookmark(product, liker);
        } else {
            throw new TypeDoesntExistsException();
        }
    }

    private void addStoreBookmark(Store store, Member liker) {  // 찜한 정보 DB에 저장
        storeBookmarkRepository.save(toEntity(store, liker));
    }

    private void addProductBookmark(Product product, Member liker) {  // 찜한 정보 DB에 저장
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
