package org.digital_academy.config;

import org.digital_academy.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
// @EnableMethodSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     return email -> userRepository.findByEmail(email)
    //             .map(user -> {
    //                 // DEBUG: Ver qué roles tiene el usuario
    //                 System.out.println("=== DEBUG USER DETAILS SERVICE ===");
    //                 System.out.println("🔍 Usuario: " + user.getEmail());
    //                 System.out.println("🔍 Roles desde BD: " + user.getRoles());
                    
    //                 // Convertir roles a autoridades correctamente
    //                 List<GrantedAuthority> authorities = user.getRoles().stream()
    //                         .map(role -> {
    //                             // Asegurar formato ROLE_XXX
    //                             String authorityName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
    //                             System.out.println("🔍 Creando autoridad: " + authorityName);
    //                             return new SimpleGrantedAuthority(authorityName);
    //                         })
    //                         .collect(Collectors.toList());
                    
    //                 System.out.println("🔍 Autoridades finales: " + authorities);
    //                 System.out.println("=== FIN DEBUG ===");
                    
    //                 return org.springframework.security.core.userdetails.User
    //                         .withUsername(user.getEmail())
    //                         .password(user.getPassword())
    //                         .authorities(authorities)
    //                         .build();
    //             })
    //             .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    // }

    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     // ✅ CORRECCIÓN: Pasar UserDetailsService al constructor
    //     DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
    //     provider.setPasswordEncoder(passwordEncoder());
    //     return provider;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configure(http))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // ← PERMITE TODO TEMPORALMENTE
            );
        
        return http.build();
    }
}