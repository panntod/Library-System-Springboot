package panntod.core.library.library_system.dto.books;

public record BookSearchRequest(
        String name,
        String author,
        String category,
        Boolean isActive
) {
}
