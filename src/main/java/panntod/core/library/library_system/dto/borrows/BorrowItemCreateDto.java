package panntod.core.library.library_system.dto.borrows;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BorrowItemCreateDto {

    @NotNull(message = "Book ID is required")
    private UUID bookId;

    private Integer qty = 1;
}
