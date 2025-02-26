//package com.sin.inforsystembackend.config;
//
//import org.springdoc.core.annotations.OpenAPIDefinition;
//import org.springdoc.core.annotations.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springdoc.core.models.Docket;
//import org.springdoc.core.models.DocumentationType;
//import org.springframework.web.bind.annotation.RequestHandlerSelectors;
//import org.springframework.web.bind.annotation.PathSelectors;
//
//
//
//@Configuration
//@OpenAPIDefinition(info = @Info(title = "InforSystem API", version = "v1", description = "API for managing the Information System Backend"))
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30) // 使用 OpenAPI 3.0 标准
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.sin.inforsystembackend.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//}