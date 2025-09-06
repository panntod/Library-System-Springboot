package panntod.core.library.library_system.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import panntod.core.library.library_system.dto.users.UserSearchRequest;
import panntod.core.library.library_system.entities.User;

public class UserSpecification {

    public static Specification<User> bySearch(UserSearchRequest searchRequest) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (searchRequest == null) {
                return predicate;
            }

            // Search by full name (firstName + lastName)
            if (searchRequest.fullname() != null && !searchRequest.fullname().isBlank()) {
                String pattern = "%" + searchRequest.fullname().toLowerCase() + "%";

                Predicate firstNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        pattern
                );

                Predicate lastNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        pattern
                );

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            // Search by address (partial match)
            if (searchRequest.address() != null && !searchRequest.address().isBlank()) {
                String pattern = "%" + searchRequest.address().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), pattern)
                );
            }

            // Search by email (exact match)
            if (searchRequest.email() != null && !searchRequest.email().isBlank()) {
                String pattern = "%" + searchRequest.email().toLowerCase() + "%";
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern)
                );
            }

            // Filter by active status
            if (searchRequest.isActive() != null) { // assuming Boolean
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("isActive"), searchRequest.isActive())
                );
            }

            if (searchRequest.role() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("role"), searchRequest.role())
                );
            }

            return predicate;
        };
    }
}
