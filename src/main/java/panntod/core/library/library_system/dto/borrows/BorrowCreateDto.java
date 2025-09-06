package panntod.core.library.library_system.dto.borrows;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BorrowCreateDto {

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    @NotNull(message = "Admin ID is required")
    private UUID adminId;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    // Daftar buku yang dipinjam (detail item)
    @NotNull(message = "Borrow items are required")
    private List<BorrowItemCreateDto> items;
}
