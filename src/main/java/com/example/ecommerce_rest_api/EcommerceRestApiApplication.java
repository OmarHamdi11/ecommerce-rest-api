package com.example.ecommerce_rest_api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Ecommerce REST API Documentation",
                description = "A comprehensive REST API for managing Ecommerce products, cart, wishlist, and user authentication. Built with Spring Boot and secured with JWT authentication.",
                version = "v1.0 ",
                contact = @Contact(
                        name = "Omar",
                        email = "omarellafy1@gmail.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Ecommerce Application Documentation",
                url = "https://github.com/OmarHamdi11/ecommerce-rest-api"
        )
)
public class EcommerceRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceRestApiApplication.class, args);
	}

}
