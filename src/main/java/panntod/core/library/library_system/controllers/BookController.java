package panntod.core.library.library_system.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import panntod.core.library.library_system.dto.books.BookCreateDto;
import panntod.core.library.library_system.dto.books.BookDto;
import panntod.core.library.library_system.dto.books.BookPatchDto;
import panntod.core.library.library_system.dto.books.BookSearchRequest;
import panntod.core.library.library_system.dto.commons.ApiResponse;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.services.BookService;
import panntod.core.library.library_system.utils.SortUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller untuk resource Books.
 * <p>
 * Endpoint:
 * - POST   /api/books              -> create produk baru
 * - GET    /api/books              -> list produk dengan pagination + sorting
 * - GET    /api/books/{id}         -> detail produk
 * - PUT    /api/books/{id}         -> update penuh
 * - PATCH  /api/books/{id}         -> update parsial
 * - DELETE /api/books/{id}         -> hapus sementara produk
 * - DELETE /api/books/hard/{id}    -> hapus permanent produk
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BookDto>> create(@Valid @RequestBody BookCreateDto dto) {
        var createBookDto = bookService.create(dto);
        return ResponseEntity.status(201).body(new ApiResponse<>("success", "SUCCESS_CREATE_BOOK", "Book created successfully", Optional.of(createBookDto)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookDto>> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(required = false) List<String> sort, @RequestParam(required = false) String name, @RequestParam(required = false) String author, @RequestParam(required = false) String category, @RequestParam(defaultValue = "false") boolean showSoftDelete) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, SortUtil.parseSortParam(sort));
        BookSearchRequest req = new BookSearchRequest(name, author, category, null);
        return ResponseEntity.ok(bookService.search(req, pageable, showSoftDelete));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> get(@PathVariable UUID id) {
        var book = bookService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "SUCCESS_GET_BOOK_BY_ID", "Book found", Optional.of(book)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BookDto>> update(@PathVariable UUID id, @Valid @RequestBody BookCreateDto dto) {
        var updated = bookService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "SUCCESS_UPDATE_BOOK", "Book updated successfully", Optional.of(updated)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BookDto>> patch(@PathVariable UUID id, @RequestBody BookPatchDto dto) {
        var patched = bookService.patch(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "SUCCESS_UPDATE_PARTIAL_BOOK", "Book partially updated", Optional.of(patched)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BookDto>> softDelete(@PathVariable UUID id) {
        bookService.softDelete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "SUCCESS_DELETE_BOOK", "Book deleted successfully", Optional.empty()));
    }

    @DeleteMapping("/hard/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BookDto>> hardDelete(@PathVariable UUID id) {
        bookService.hardDelete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "SUCCESS_DELETE_BOOK", "Book deleted successfully", Optional.empty()));
    }
}
