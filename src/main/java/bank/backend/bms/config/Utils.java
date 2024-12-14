package bank.backend.bms.config;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.context.annotation.Bean;

public final class Utils {
  @Bean
  public static Timestamp now() {
    return Timestamp.from(Instant.now());
  }
}
