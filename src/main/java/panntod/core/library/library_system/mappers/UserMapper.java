package panntod.core.library.library_system.mappers;

import org.mapstruct.Mapper;
import panntod.core.library.library_system.dto.users.UserCreateDto;
import panntod.core.library.library_system.dto.users.UserDto;
import panntod.core.library.library_system.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity -> DTO
    UserDto toDto(User user);

    // DTO -> Entity
    User toEntity(UserCreateDto dto);
}
