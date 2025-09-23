package org.digital_academy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableMethodSecurity 




public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/error").permitAll() 
                .requestMatchers("/patients/**").hasAnyRole("ADMIN", "USER") 
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable()) 
            .logout(logout -> logout.permitAll());

        return http.build();
    }

   
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas y Swagger
            .authorizeHttpRequests(auth -> auth
                // Permitimos acceso libre a appointments y Swagger
                .requestMatchers("/appointments/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // permite autenticación básica para pruebas
        return http.build();
    }
}
