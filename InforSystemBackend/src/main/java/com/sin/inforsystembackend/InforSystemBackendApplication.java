package com.sin.inforsystembackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "InforSystem Backend API", version = "v1", description = "API for managing the Information System Backend"))
public class InforSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InforSystemBackendApplication.class, args);
    }
}