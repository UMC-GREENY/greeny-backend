package greeny.backend.domain.review.entity;

import greeny.backend.exception.situation.TypeDoesntExistsException;

import java.util.Comparator;

public class ReviewComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof ProductReview && o2 instanceof ProductReview) {
            Long id1 = ((ProductReview) o1).getId();
            Long id2 = ((ProductReview) o2).getId();
            return id1.compareTo(id2);
        } else if (o1 instanceof StoreReview && o2 instanceof ProductReview) {
            Long id1 = ((StoreReview) o1).getId();
            Long id2 = ((ProductReview) o2).getId();
            return id1.compareTo(id2);
        } else if (o1 instanceof StoreReview && o2 instanceof StoreReview) {
            Long id1 = ((StoreReview) o1).getId();
            Long id2 = ((StoreReview) o2).getId();
            return id1.compareTo(id2);
        } else if (o1 instanceof ProductReview && o2 instanceof StoreReview) {
            Long id1 = ((ProductReview) o1).getId();
            Long id2 = ((StoreReview) o2).getId();
            return id1.compareTo(id2);
            // Add other entity type comparisons if needed
            // Handle other cases, or consider the IDs equal for different entity types
        } else throw new TypeDoesntExistsException();
    }
}
