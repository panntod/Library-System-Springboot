package panntod.core.library.library_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // bisa ganti dengan domain tertentu
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(true);
    }
}
