package com.finanzen.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching // Liga o motor de interceptação do Spring
@SpringBootApplication
public class FinanzenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanzenApiApplication.class, args);
	}

}
