package panntod.core.library.library_system.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import panntod.core.library.library_system.dto.borrows.BorrowSearchRequest;
import panntod.core.library.library_system.entities.Borrow;

public class BorrowSpecification {

    public static Specification<Borrow> bySearch(BorrowSearchRequest searchRequest) {
        return (Root<Borrow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (searchRequest == null) {
                return predicate;
            }

            // Filter by memberId
            if (searchRequest.getMemberId() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("member").get("id"), searchRequest.getMemberId())
                );
            }

            // Filter by adminId
            if (searchRequest.getAdminId() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("admin").get("id"), searchRequest.getAdminId())
                );
            }

            // Filter by status
            if (searchRequest.getStatus() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), searchRequest.getStatus())
                );
            }

            // Filter by borrowDate range
            if (searchRequest.getBorrowDateFrom() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("borrowDate"), searchRequest.getBorrowDateFrom())
                );
            }

            if (searchRequest.getBorrowDateTo() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("borrowDate"), searchRequest.getBorrowDateTo())
                );
            }

            if (searchRequest.getPenaltyFrom() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("penalty"), searchRequest.getPenaltyFrom())
                );
            }

            if (searchRequest.getPenaltyTo() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("penalty"), searchRequest.getPenaltyTo())
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
