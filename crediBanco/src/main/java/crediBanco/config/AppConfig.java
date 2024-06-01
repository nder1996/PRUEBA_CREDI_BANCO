package crediBanco.config;

import crediBanco.util.ValidationUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtil();
    }

}
