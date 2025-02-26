//package com.sin.inforsystembackend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // 禁用CSRF保护
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll() // 允许所有请求，不做任何验证
//                );
//
//        return http.build();
//    }
//}