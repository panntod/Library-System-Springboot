package panntod.core.library.library_system.dto.books;

public record BookPatchDto(
        String name,
        String author,
        Integer stocks,
        String category,
        Boolean isActive
) {
}
