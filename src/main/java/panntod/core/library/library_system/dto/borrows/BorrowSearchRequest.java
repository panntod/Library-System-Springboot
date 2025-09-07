package panntod.core.library.library_system.dto.borrows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import panntod.core.library.library_system.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowSearchRequest {
    private UUID memberId;
    private UUID adminId;
    private BorrowStatus status;
    private LocalDateTime borrowDateFrom;
    private LocalDateTime borrowDateTo;
    private Double penaltyFrom;
    private Double penaltyTo;
    private Boolean isActive;
}