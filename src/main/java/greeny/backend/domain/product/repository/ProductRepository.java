package greeny.backend.domain.product.repository;

import greeny.backend.domain.product.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p left join fetch p.store left join fetch p.bookmarks left join fetch p.reviews")
    @NotNull
    List<Product> findProductsWithStoreAndBookmarksAndReviews();

    @EntityGraph(attributePaths = {"store"})
    @NotNull
    Optional<Product> findProductById(Long id);
}
