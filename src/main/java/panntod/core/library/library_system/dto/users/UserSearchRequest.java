package panntod.core.library.library_system.dto.users;

public record UserSearchRequest(
        String fullname,
        String email,
        String address,
        Boolean isActive
) {}
