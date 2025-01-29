package com.example.crud_demo_docker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class configuration { // ✅ Renamed to avoid conflict

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for API access
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v2/api-docs/**",
                                "/v2/api-docs.yaml",
                                "/v2/api-docs.json",
                                "/webjars/**"
                        ).permitAll() // ✅ Allow unrestricted access to Swagger
                        .anyRequest().permitAll() // ✅ Allow all requests
                );

        return http.build();
    }
}
