package com.microservice_authenticate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceAuthenticateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthenticateApplication.class, args);
	}

}
