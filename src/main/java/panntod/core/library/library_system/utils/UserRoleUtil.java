package panntod.core.library.library_system.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import panntod.core.library.library_system.enums.UserRole;

public class UserRoleUtil {
    public static UserRole getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            return UserRole.valueOf(role.replace("ROLE_", ""));
        }
        throw new AccessDeniedException("Role not found in security context");
    }
}
