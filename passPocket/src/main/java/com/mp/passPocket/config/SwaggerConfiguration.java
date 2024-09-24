package com.mp.passPocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfiguration {
	
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo()).addServersItem(new Server().url("http://localhost:9090"));
	}

	public Info apiInfo() {
		return new Info()
				.title("PassPort API Test With Swagger")
				.description("-- description --")
				.version("1.0.0");
		
	}
}

