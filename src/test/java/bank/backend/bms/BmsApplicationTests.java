package bank.backend.bms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BmsApplicationTests {

	@Test
	void contextLoads() {
	}

}
