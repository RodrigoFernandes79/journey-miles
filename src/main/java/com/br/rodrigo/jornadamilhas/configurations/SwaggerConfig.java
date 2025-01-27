package com.br.rodrigo.jornadamilhas.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customSwaggerConfig() {
        return new OpenAPI().info(new Info()
                .title("Journey Miles Api")
                .version("v1")
                .description("Reservations flights Api")
                .contact(new Contact().url("https://github.com/RodrigoFernandes79"))
        );
    }
}
