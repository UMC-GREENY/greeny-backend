package greeny.backend.domain.store.repository;

import greeny.backend.domain.store.entity.Store;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecification {
    public static Specification<Store> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<Store> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("location"), "%" + location + "%");
    }
    public static Specification<Store> equalCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }
}
