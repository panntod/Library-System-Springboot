package panntod.core.library.library_system.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import panntod.core.library.library_system.entities.User;
import panntod.core.library.library_system.repositories.UserRepository;
import panntod.core.library.library_system.utils.JwtUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthentication extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                UUID userId = JwtUtil.getUserIdFromToken(token, "access");

                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    var auth = new UsernamePasswordAuthenticationToken(
                            user, null, null
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("error_code", "TOKEN_EXPIRED");
            } catch (Exception e) {
                request.setAttribute("error_code", "UNAUTHORIZED");
            }
        }

        filterChain.doFilter(request, response);
    }
}
