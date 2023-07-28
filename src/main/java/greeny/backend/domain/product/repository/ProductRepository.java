package greeny.backend.domain.product.repository;

import greeny.backend.domain.product.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"store", "reviews", "bookmarks"})  // Collection fetch join -> DB 과부하 방지를 위해 batch size 설정
    @NotNull
    List<Product> findAll();
}
