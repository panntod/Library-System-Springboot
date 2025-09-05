package panntod.core.library.library_system.dto.users;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String fullName,
        String firstName,
        String lastName,
        String email,
        String address,
        String phoneNumber,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
