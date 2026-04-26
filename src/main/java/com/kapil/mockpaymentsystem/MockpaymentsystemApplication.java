package com.kapil.mockpaymentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.kapil.mockpaymentsystem")
public class MockpaymentsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockpaymentsystemApplication.class, args);
	}

}
