package greeny.backend.domain.product.service;

import greeny.backend.domain.bookmark.entity.ProductBookmark;
import greeny.backend.domain.product.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.exception.situation.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<GetSimpleProductInfosResponseDto> getSimpleProductInfos(){

        return productRepository.findAll().stream()
                .map(product -> GetSimpleProductInfosResponseDto.from(product,product.getStore().getName(),product.getBookmarks().size(),product.getReviews().size()))
                .collect(Collectors.toList());
    }

}
