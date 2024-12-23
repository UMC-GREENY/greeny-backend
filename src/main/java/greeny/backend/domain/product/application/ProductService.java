package greeny.backend.domain.product.application;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.product.presentation.dto.GetProductInfoResponseDto;
import greeny.backend.domain.product.presentation.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.entity.ProductRepository;
import greeny.backend.exception.situation.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Page<GetSimpleProductInfosResponseDto> getSimpleProductInfos(String keyword, Pageable pageable) {

        if (StringUtils.hasText(keyword)){
            return productRepository.findProductsByNameContainingIgnoreCase(keyword,pageable)
                    .map(product -> GetSimpleProductInfosResponseDto.from(product,false));
        }

        return productRepository.findAll(pageable)
                .map(product -> GetSimpleProductInfosResponseDto.from(product,false));
    }

    @Transactional
    public Page<GetSimpleProductInfosResponseDto> getSimpleProductInfosWithAuthMember(String keyword,List<ProductBookmark> productBookmarks, Pageable pageable){

        List<GetSimpleProductInfosResponseDto> simpleProductInfos = new ArrayList<>();

        if(StringUtils.hasText(keyword)){
            return checkBookmarkedProduct(
                    productRepository.findProductsByNameContainingIgnoreCase(keyword,pageable).getContent(),
                    productBookmarks,
                    pageable
            );
        }
        return checkBookmarkedProduct(productRepository.findAll(pageable).getContent(),productBookmarks,pageable);
    }

    public GetProductInfoResponseDto getProductInfo(Long productId){
        Product foundProduct = getProduct(productId);
        return GetProductInfoResponseDto.from(foundProduct, false);
    }

    public GetProductInfoResponseDto getProductInfoWithAuthMember(Long productId, Optional<ProductBookmark> optionalProductBookmark) {

        if(optionalProductBookmark.isPresent())
            return GetProductInfoResponseDto.from(getProduct(productId), true);

        return GetProductInfoResponseDto.from(getProduct(productId), false);
    }

    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Page<GetSimpleProductInfosResponseDto> checkBookmarkedProduct(List<Product> products, List<ProductBookmark> productBookmarks, Pageable pageable){
        List<GetSimpleProductInfosResponseDto> simpleProducts = new ArrayList<>();

        for (Product product: products) {
            boolean isBookmarked =false;

            for (ProductBookmark productBookmark: productBookmarks){
                if(productBookmark.getProduct().getId().equals(product.getId())){
                    isBookmarked =true;
                    simpleProducts.add(GetSimpleProductInfosResponseDto.from(product,isBookmarked));
                    productBookmarks.remove(productBookmark);
                    break;
                }
            }
            if(!isBookmarked){
                simpleProducts.add(GetSimpleProductInfosResponseDto.from(product,isBookmarked));
            }
        }
        return new PageImpl<>(
                simpleProducts,
                pageable,
                simpleProducts.size()
        );
    }
}