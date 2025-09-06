package panntod.core.library.library_system.mappers;

import org.mapstruct.Mapper;
import panntod.core.library.library_system.dto.books.BookCreateDto;
import panntod.core.library.library_system.dto.books.BookDto;
import panntod.core.library.library_system.entities.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
    Book toEntity(BookCreateDto dto);
}
