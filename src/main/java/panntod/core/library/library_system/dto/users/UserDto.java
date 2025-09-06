package panntod.core.library.library_system.dto.users;

import panntod.core.library.library_system.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String fullName,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        String address,
        String phoneNumber,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
