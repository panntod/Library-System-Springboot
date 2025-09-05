package panntod.core.library.library_system.dto.commons;

import java.util.Optional;

public record ApiResponse<T>(
        String status,
        String code,
        String message,
        Optional<T> data
) {
    public ApiResponse(String status, String code, String message) {
        this(status, code, message, Optional.empty());
    }
}