package com.finanzen.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinanzenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanzenApiApplication.class, args);
	}

}
