package greeny.backend.domain.product.repository;

import greeny.backend.domain.product.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"store", "reviews", "bookmarks"})
    @NotNull
    List<Product> findAll();

    @EntityGraph(attributePaths = {"store"})
    @NotNull
    Optional<Product> findProductById(Long id);
}
