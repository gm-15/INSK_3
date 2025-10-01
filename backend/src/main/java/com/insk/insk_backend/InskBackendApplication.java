package com.insk.insk_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 기능 활성화
@SpringBootApplication
public class InskBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InskBackendApplication.class, args);
	}

}
