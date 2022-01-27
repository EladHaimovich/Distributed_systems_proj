package CryptoSystem.SystemServer.Spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SystemServerApplication {

	public static void main_spring(String[] args) {
		SpringApplication app = new SpringApplication(SystemServerApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", Integer.parseInt(args[0])));
		app.run(args);

	}





}
