package panntod.core.library.library_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import panntod.core.library.library_system.dto.borrows.BorrowItemCreateDto;
import panntod.core.library.library_system.dto.borrows.BorrowItemDto;
import panntod.core.library.library_system.entities.BorrowItem;

@Mapper(componentModel = "spring")
public interface BorrowItemMapper {

    // Entity -> DTO
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.name", target = "bookName")
    BorrowItemDto toDto(BorrowItem borrowItem);

    // DTO -> Entity
    BorrowItem toEntity(BorrowItemCreateDto dto);
}
