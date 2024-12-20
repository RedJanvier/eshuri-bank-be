package bank.backend.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    public static String hashPassword(String s) {
      return passwordEncoder().encode(s);
    }
}
