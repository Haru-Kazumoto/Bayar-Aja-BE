package project.bayaraja.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BayarAjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BayarAjaApplication.class, args);
	}

}
