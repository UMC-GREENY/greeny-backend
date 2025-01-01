package greeny.backend.domain.store.entity;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class StoreSpecification {

    public static Specification<Store> create(Specification<Store> spec, String keyword, String location, String category) {
        return Specification.where(spec)
                .and(whereKeyword(keyword))
                .and(whereLocation(location))
                .and(whereCategory(category));
    }

    private static Specification<Store> whereKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? hasKeyword(keyword) : (root, query, criteriaBuilder) -> null;
    }

    private static Specification<Store> whereLocation(String location) {
        return StringUtils.hasText(location) ? hasLocation(location) : (root, query, criteriaBuilder) -> null;
    }

    private static Specification<Store> whereCategory(String category) {
        return StringUtils.hasText(category) ? equalCategory(category) : (root, query, criteriaBuilder) -> null;
    }

    private static Specification<Store> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }

    private static Specification<Store> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("location"), "%" + location + "%");
    }

    private static Specification<Store> equalCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }
}