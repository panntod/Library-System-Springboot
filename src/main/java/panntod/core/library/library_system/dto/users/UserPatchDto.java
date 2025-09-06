package panntod.core.library.library_system.dto.users;

import panntod.core.library.library_system.enums.UserRole;

public record UserPatchDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String address,
        String phoneNumber,
        Boolean isActive,
        UserRole role
) {}
