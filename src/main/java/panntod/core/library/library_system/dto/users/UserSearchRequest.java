package panntod.core.library.library_system.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import panntod.core.library.library_system.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private String fullname;
    private String email;
    private String address;
    private Boolean isActive;
    private UserRole role;
}
