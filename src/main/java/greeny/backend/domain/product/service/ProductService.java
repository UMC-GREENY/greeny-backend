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
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    // 제품 목록 가져오기

    public List<GetSimpleProductInfosResponseDto> getSimpleProductInfos(List<ProductBookmark> myProductBookmarks){
        return productRepository.findAll().stream()
                .map(product -> GetSimpleProductInfosResponseDto.from(product,product.getStore().getName(),product.getBookmarks().size(),product.getReviews().size()))
                .collect(Collectors.toList());
    }

    // 제품 상세목록 가져오기
    public GetProductInfoResponseDto getProductInfo(Long productId){
        Product foundProduct = getProduct(productId);
        Store store = foundProduct.getStore();
        return GetProductInfoResponseDto.from(foundProduct, store.getName(), store.getImageUrl());
    }

    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
