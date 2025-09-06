package panntod.core.library.library_system.dto.commons;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
