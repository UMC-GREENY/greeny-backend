package greeny.backend.domain.product.service;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.product.dto.GetProductInfoResponseDto;
import greeny.backend.domain.product.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.exception.situation.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    // 모든 사용자에게 제품 목록 보여주기
    public List<GetSimpleProductInfosResponseDto> getSimpleProductInfos() {
        return productRepository.findProductsWithStoreAndBookmarksAndReviews().stream()
                .map(product ->
                        GetSimpleProductInfosResponseDto.from(
                                product,
                                product.getStore().getName(),
                                product.getBookmarks().size(),
                                product.getReviews().size(),
                                false
                        )
                )
                .collect(Collectors.toList());
    }

    // 인증된 사용자의 제품 목록 가져오기
    public List<GetSimpleProductInfosResponseDto> getSimpleProductInfosWithAuthMember(List<ProductBookmark> productBookmarks){

        List<GetSimpleProductInfosResponseDto> simpleProductInfos = new ArrayList<>();
        List<Product> foundProducts = productRepository.findProductsWithStoreAndBookmarksAndReviews();

        for(Product product : foundProducts) {

            boolean isBookmarked = false;

            for(ProductBookmark productBookmark : productBookmarks) {
                if(productBookmark.getProduct().getId().equals(product.getId())) {
                    isBookmarked = true;
                    simpleProductInfos.add(
                            GetSimpleProductInfosResponseDto.from(
                                    product,
                                    product.getStore().getName(),
                                    product.getBookmarks().size(),
                                    product.getReviews().size(),
                                    isBookmarked
                            )
                    );
                    break;
                }
            }

            if(!isBookmarked) {
                simpleProductInfos.add(
                        GetSimpleProductInfosResponseDto.from(
                                product,
                                product.getStore().getName(),
                                product.getBookmarks().size(),
                                product.getReviews().size(),
                                isBookmarked
                        )
                );
            }
        }

        return simpleProductInfos;
    }

    // 제품 상세목록 가져오기
    public GetProductInfoResponseDto getProductInfo(Long productId){
        Product foundProduct = getProduct(productId);
        Store store = foundProduct.getStore();
        return GetProductInfoResponseDto.from(foundProduct, store.getName(), store.getWebUrl());
    }

    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
