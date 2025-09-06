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
            if (searchRequest.getFullname() != null && !searchRequest.getFullname().isBlank()) {
                String pattern = "%" + searchRequest.getFullname().toLowerCase() + "%";

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
            if (searchRequest.getAddress() != null && !searchRequest.getAddress().isBlank()) {
                String pattern = "%" + searchRequest.getAddress().toLowerCase() + "%";

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), pattern)
                );
            }

            // Search by email (exact match)
            if (searchRequest.getEmail() != null && !searchRequest.getEmail().isBlank()) {
                String pattern = "%" + searchRequest.getEmail().toLowerCase() + "%";
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern)
                );
            }

            // Filter by active status
            if (searchRequest.getIsActive() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("isActive"), searchRequest.getIsActive())
                );
            }

            if (searchRequest.getRole() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("role"), searchRequest.getRole())
                );
            }

            return predicate;
        };
    }
}
