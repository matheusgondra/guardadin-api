package com.matheusgondra.guardadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GuardaDinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardaDinApiApplication.class, args);
	}

}
