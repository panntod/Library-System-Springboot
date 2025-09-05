package panntod.core.library.library_system.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String status;
    private String code;
    private String message;
    private Map<String, String> errors;

    public ApiError(String status, String code, Map<String, String> errors) {
        this.status = status;
        this.code = code;
        this.errors = errors;
    }

    public ApiError(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
