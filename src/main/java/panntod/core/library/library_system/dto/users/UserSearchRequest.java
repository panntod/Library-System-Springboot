package panntod.core.library.library_system.dto.users;

import panntod.core.library.library_system.enums.UserRole;

public record UserSearchRequest(
        String fullname,
        String email,
        String address,
        Boolean isActive,
        UserRole role
) {}
