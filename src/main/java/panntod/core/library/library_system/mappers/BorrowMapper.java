package panntod.core.library.library_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import panntod.core.library.library_system.dto.borrows.BorrowCreateDto;
import panntod.core.library.library_system.dto.borrows.BorrowDto;
import panntod.core.library.library_system.entities.Borrow;

@Mapper(componentModel = "spring", uses = {BorrowItemMapper.class})
public interface BorrowMapper {

    // Entity -> DTO
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "member.fullName", target = "memberName")
    @Mapping(source = "admin.id", target = "adminId")
    @Mapping(source = "admin.fullName", target = "adminName")
    @Mapping(source = "status", target = "status")
    BorrowDto toDto(Borrow borrow);

    // DTO -> Entity
    Borrow toEntity(BorrowCreateDto dto);
}
