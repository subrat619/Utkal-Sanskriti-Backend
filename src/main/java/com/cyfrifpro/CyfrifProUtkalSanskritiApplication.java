package com.cyfrifpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class CyfrifProUtkalSanskritiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyfrifProUtkalSanskritiApplication.class, args);
	}

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Utkal Sanskriti App").description("Backend APIs Utkal Sanskriti App")
						.version("v1.0.0")
						.contact(new Contact().name("Cyfrifpro tech").url("https://www.cyfrifprotech.com/")
								.email("tech.ho@cyfrif.com"))
						.license(new License().name("License").url("/")))
				.externalDocs(new ExternalDocumentation().description("Utkal Sanskriti App Documentation")
						.url("http://localhost:8080/swagger-ui/index.html"));
	}

}
