package greeny.backend.domain.product.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"store"})
    @NotNull
    Optional<Product> findProductById(Long id);

    Page<Product> findProductsByNameContainingIgnoreCase(String keyword, Pageable pageable);
}