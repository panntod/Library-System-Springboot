package panntod.core.library.library_system.dto.books;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookDto (
        UUID id,
        String name,
        String author,
        Integer stocks,
        String category,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
