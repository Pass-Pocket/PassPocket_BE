package com.mp.passPocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PassPocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassPocketApplication.class, args);
	}

}
