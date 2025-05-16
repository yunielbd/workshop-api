package com.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class VctrlApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(VctrlApplication.class, args);
	}

}
