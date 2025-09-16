package org.digital_academy.clinicaveterinaria;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Necesario para poder hacer POST/PUT/DELETE desde Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/pacientes/**").permitAll() // ✅ ahora permite GET/POST/PUT/DELETE en todos los subpaths
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
