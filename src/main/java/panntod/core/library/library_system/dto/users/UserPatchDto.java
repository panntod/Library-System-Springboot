package panntod.core.library.library_system.dto.users;

public record UserPatchDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String address,
        String phoneNumber,
        Boolean isActive
) {}
