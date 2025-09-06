package panntod.core.library.library_system.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import panntod.core.library.library_system.dto.borrows.BorrowSearchRequest;
import panntod.core.library.library_system.entities.Borrow;

import java.util.UUID;

public class BorrowSpecification {

    public static Specification<Borrow> bySearch(BorrowSearchRequest searchRequest) {
        return (Root<Borrow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (searchRequest == null) {
                return predicate;
            }

            // Filter by memberId
            if (searchRequest.memberId() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("member").get("id"), searchRequest.memberId())
                );
            }

            // Filter by adminId
            if (searchRequest.adminId() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("admin").get("id"), searchRequest.adminId())
                );
            }

            // Filter by status
            if (searchRequest.status() != null && !searchRequest.status().isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), searchRequest.status().toLowerCase())
                );
            }

            // Filter by borrowDate range
            if (searchRequest.borrowDateFrom() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("borrowDate"), searchRequest.borrowDateFrom())
                );
            }

            if (searchRequest.borrowDateTo() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("borrowDate"), searchRequest.borrowDateTo())
                );
            }

            if (searchRequest.penaltyFrom() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("penalty"), searchRequest.penaltyFrom())
                );
            }

            if (searchRequest.penaltyTo() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("penalty"), searchRequest.penaltyTo())
                );
            }

            return predicate;
        };
    }
}
