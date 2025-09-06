package panntod.core.library.library_system.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import panntod.core.library.library_system.dto.books.BookSearchRequest;
import panntod.core.library.library_system.entities.Book;

public class BookSpecification {
    public static Specification<Book> bySearch(BookSearchRequest searchRequest) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (searchRequest == null) {
                return predicate;
            }

            if (searchRequest.getName() != null && !searchRequest.getName().isBlank()) {
                String pattern = "%" + searchRequest.getName().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern)
                );
            }

            if (searchRequest.getAuthor() != null && !searchRequest.getAuthor().isBlank()) {
                String pattern = "%" + searchRequest.getAuthor().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
                );
            }

            if (searchRequest.getCategory() != null && !searchRequest.getCategory().isBlank()) {
                String pattern = "%" + searchRequest.getCategory().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
                );
            }

            if (searchRequest.getIsActive() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("isActive"), searchRequest.getIsActive())
                );
            }

            return predicate;
        };
    }
}
