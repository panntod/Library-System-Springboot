package panntod.core.library.library_system.dto.borrows;

import java.util.UUID;

public record BorrowItemDto(
        UUID id,
        UUID bookId,
        String bookName,
        Integer qty
) {}
