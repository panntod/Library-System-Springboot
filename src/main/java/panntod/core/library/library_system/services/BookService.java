package panntod.core.library.library_system.services;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panntod.core.library.library_system.dto.books.*;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.entities.Book;
import panntod.core.library.library_system.mappers.BookMapper;
import panntod.core.library.library_system.repositories.BookRepository;
import panntod.core.library.library_system.specs.BookSpecification;

import java.util.UUID;

@Service
public class BookService {
    private final BookRepository repo;
    private final BookMapper mapper;

    public BookService(BookRepository repo, BookMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public BookDto create(BookCreateDto createDto) {
        Book book = mapper.toEntity(createDto);
        Book savedBook = repo.save(book);
        return mapper.toDto(savedBook);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookDto> search(BookSearchRequest req, Pageable pageable) {
        var spec = BookSpecification.bySearch(req);
        Page<Book> page = repo.findAll(spec, pageable);

        var content = page.stream()
                .map(mapper::toDto)
                .toList();

        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public BookDto findById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Transactional
    public BookDto update(UUID id, BookCreateDto updateDto) {
        Book exististing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        exististing.setName(updateDto.getName());
        exististing.setAuthor(updateDto.getAuthor());
        exististing.setCategory(updateDto.getCategory());
        exististing.setStocks(updateDto.getStocks());

        // Only Super Admin
        exististing.setIsActive(updateDto.getIsActive());

        Book saved = repo.save(exististing);
        return mapper.toDto(saved);
    }

    @Transactional
    public BookDto patch(UUID id, BookPatchDto patchDto) {
        Book existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (patchDto.name() != null) existing.setName(patchDto.name());
        if (patchDto.author() != null) existing.setAuthor(patchDto.author());
        if (patchDto.category() != null) existing.setCategory(patchDto.category());
        if (patchDto.stocks() != null) existing.setStocks(patchDto.stocks());

        // Only Super Admin
        if (patchDto.isActive() != null) existing.setIsActive(patchDto.isActive());

        Book saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        repo.deleteById(id);
    }
}
