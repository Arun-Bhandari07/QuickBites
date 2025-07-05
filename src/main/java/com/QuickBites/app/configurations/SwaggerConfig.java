package com.QuickBites.app.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI swaggerCustomization() {
		return new OpenAPI()
				.info(
					new Info()
						.title("QuickBites")
						.description("API Documentation for QuickBites App")
					 )
				.servers(
					List.of(
					new Server().url("http://localhost:8080").description("Local Development")
					,new Server().url("www.QuickBites.com")
						)
					);
		
	}
}
