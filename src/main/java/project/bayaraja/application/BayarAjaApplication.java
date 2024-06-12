package project.bayaraja.application;

import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import project.bayaraja.application.utils.filestorage.FileStorageService;

@SpringBootApplication
@EnableTransactionManagement
public class BayarAjaApplication implements CommandLineRunner {

	@Resource
	FileStorageService fileStorageService;

	public static void main(String[] args) {
		SpringApplication.run(BayarAjaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.fileStorageService.init();
	}
}
