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

            if (searchRequest.name() != null && !searchRequest.name().isBlank()) {
                String pattern = "%" + searchRequest.name().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern)
                );
            }

            if (searchRequest.author() != null && !searchRequest.author().isBlank()) {
                String pattern = "%" + searchRequest.author().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
                );
            }

            if(searchRequest.category() != null && !searchRequest.category().isBlank()) {
                String pattern = "%" + searchRequest.category().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
                );
            }

            return predicate;
        };
    }
}
