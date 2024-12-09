package bank.backend.bms;

import org.springframework.boot.SpringApplication;

public class TestBmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(BmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
