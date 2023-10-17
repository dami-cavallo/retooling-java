package com.retooling.accenture.msspringsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.retooling.accenture.msspringsecurity")
public class MsSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSpringSecurityApplication.class, args);
	}

}

