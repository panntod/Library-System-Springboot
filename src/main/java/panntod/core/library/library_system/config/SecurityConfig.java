// Prod
package panntod.core.library.library_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import panntod.core.library.library_system.exceptions.ApiError;
import panntod.core.library.library_system.security.JwtAuthentication;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthentication jwtFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/refresh-token").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    // Handler untuk 401 Unauthorized
    private AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            String errorCode = (String) request.getAttribute("error_code");
            ApiError apiError;

            if ("TOKEN_EXPIRED".equals(errorCode)) {
                apiError = new ApiError("error", "TOKEN_EXPIRED", "Your session has expired. Please login again.");
            } else {
                apiError = new ApiError("error", "UNAUTHORIZED", "You need to login to access this resource");
            }

            response.getWriter().write(objectMapper.writeValueAsString(apiError));
        };
    }

    // Handler untuk 403 Forbidden
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            ApiError apiError = new ApiError("error", "FORBIDDEN", "You do not have permission to access this resource");

            response.getWriter().write(objectMapper.writeValueAsString(apiError));
        };
    }
}
