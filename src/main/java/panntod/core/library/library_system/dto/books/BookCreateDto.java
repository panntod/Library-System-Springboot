package panntod.core.library.library_system.dto.books;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Author name is required")
    private String author;

    private Integer stocks;

    @NotBlank(message = "Category is required")
    private String category;

    private Boolean isActive;
}
