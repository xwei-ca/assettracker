package com.example.assettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AssettrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssettrackerApplication.class, args);
	}

	// https://spring.io/guides/gs/rest-service-cors
	// Global CORS configuration
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// The following endpoints can be accessed from "http://localhost:9080" which runs a sample GWT app 
				// Endpoint not added in the registry will be blocked due to the 
				// Cross-Origin Resource Sharing (CORS) policy
				// even if it returns a http code 200(OK), in this case the 403(Forbidden) will show up
				registry.addMapping("/api/assets").allowedOrigins("http://localhost:9080");
				registry.addMapping("/api/assets/{id}").allowedOrigins("http://localhost:9080");
			}
		};
	}

}
