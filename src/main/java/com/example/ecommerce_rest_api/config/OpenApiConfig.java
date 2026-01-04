package com.example.ecommerce_rest_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    @Profile("prod")
    public OpenAPI prodOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("https://spotty-tanhya-ellafy-7e7c9f16.koyeb.app")
                                .description("Production Server")
                ));
    }

    @Bean
    @Profile("!prod")
    public OpenAPI devOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
}
