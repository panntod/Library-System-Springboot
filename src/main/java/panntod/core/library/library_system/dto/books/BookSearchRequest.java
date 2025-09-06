package panntod.core.library.library_system.dto.books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchRequest {
    private String name;
    private String author;
    private String category;
    private Boolean isActive;
}
