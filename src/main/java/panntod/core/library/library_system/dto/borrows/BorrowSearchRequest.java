package panntod.core.library.library_system.dto.borrows;

import java.time.LocalDateTime;
import java.util.UUID;

public record BorrowSearchRequest(
        UUID memberId,
        UUID adminId,
        String status,
        LocalDateTime borrowDateFrom,
        LocalDateTime borrowDateTo,
        Double penaltyFrom,
        Double penaltyTo
) {}
