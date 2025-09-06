package panntod.core.library.library_system.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import panntod.core.library.library_system.dto.borrows.*;
import panntod.core.library.library_system.dto.commons.ApiResponse;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.services.BorrowService;
import panntod.core.library.library_system.utils.SortUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    /**
     * CREATE Borrow
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BorrowDto>> create(@Valid @RequestBody BorrowCreateDto dto) {
        var created = borrowService.create(dto);
        return ResponseEntity.status(201).body(
                new ApiResponse<>("success", "SUCCESS_CREATE_BORROW", "Borrow transaction created successfully", Optional.of(created))
        );
    }

    /**
     * LIST Borrow transactions (paginated & filterable)
     */
    @GetMapping
    public ResponseEntity<PageResponse<BorrowDto>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID adminId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime borrowDateFrom,
            @RequestParam(required = false) LocalDateTime borrowDateTo,
            @RequestParam(required = false) Double penaltyFrom,
            @RequestParam(required = false) Double penaltyTo
    ) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, SortUtil.parseSortParam(sort));
        BorrowSearchRequest req = new BorrowSearchRequest(memberId, adminId, status, borrowDateFrom, borrowDateTo, penaltyFrom, penaltyTo);
        return ResponseEntity.ok(borrowService.search(req, pageable));
    }

    /**
     * GET Borrow by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowDto>> get(@PathVariable UUID id) {
        var borrow = borrowService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("success", "SUCCESS_GET_BORROW_BY_ID", "Borrow transaction found", Optional.of(borrow))
        );
    }

    /**
     * RETURN Borrow
     */
    @PostMapping("/{borrowId}/return")
    public ResponseEntity<ApiResponse<BorrowDto>> returnBorrow(
            @PathVariable UUID borrowId,
            @RequestParam UUID adminId
    ) {
        var returned = borrowService.returnBorrow(borrowId, adminId);
        return ResponseEntity.ok(
                new ApiResponse<>("success", "SUCCESS_RETURN_BORROW", "Borrow returned successfully", Optional.of(returned))
        );
    }

    /**
     * DELETE Borrow
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        borrowService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("success", "SUCCESS_DELETE_BORROW", "Borrow transaction deleted successfully", Optional.empty())
        );
    }
}
