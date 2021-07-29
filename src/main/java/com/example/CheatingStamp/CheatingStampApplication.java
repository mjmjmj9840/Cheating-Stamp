package com.example.CheatingStamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CheatingStampApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheatingStampApplication.class, args);
	}

}
