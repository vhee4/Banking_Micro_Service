package com.IdentityRegistry.IdentityRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityRegistryApplication {
//	@Bean
//	public WebMvcConfigurer webMvcConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//
//				registry
//						.addMapping("/**")
//						.allowedOrigins("*", "http://localhost:8081")
//						.allowedHeaders("*")
//						.allowedMethods("POST", "GET", "PUT", "PATCH", "OPTIONS");
//			}
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(IdentityRegistryApplication.class, args);
	}

}
