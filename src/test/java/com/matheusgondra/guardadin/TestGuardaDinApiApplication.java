package com.matheusgondra.guardadin;

import org.springframework.boot.SpringApplication;

public class TestGuardaDinApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(GuardaDinApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
