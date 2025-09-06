package panntod.core.library.library_system.dto.borrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BorrowDto(
        UUID id,
        UUID memberId,
        String memberName,
        UUID adminId,
        String adminName,
        LocalDateTime borrowDate,
        LocalDateTime dueDate,
        LocalDateTime returnDate,
        Double penalty,
        String status,
        List<BorrowItemDto> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
