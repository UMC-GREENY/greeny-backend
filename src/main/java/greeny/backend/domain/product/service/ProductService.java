package greeny.backend.domain.product.service;

import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.exception.situation.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }


}
