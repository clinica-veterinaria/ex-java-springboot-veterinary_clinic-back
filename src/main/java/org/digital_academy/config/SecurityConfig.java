package org.digital_academy.config;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.digital_academy.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Bean para codificar contraseñas, necesario para AuthController
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean de AuthenticationManager para autenticación manual si es necesario
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Configuración de seguridad unificada
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Solo login y registro públicos
                        .requestMatchers("/auth/**", "/error").permitAll()
                        // Endpoints solo para ADMIN
                        .requestMatchers("/appointments/**", "/treatments/**").hasRole("ADMIN")
                        // Endpoints para ADMIN y USER
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "USER")
                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated())
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}