package org.digital_academy.clinicaveterinaria.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.digital_academy.clinicaveterinaria.user.JpaUserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${api.endpoint}")
        String endpoint;

        private final JpaUserDetailService userDetailService;

        public SecurityConfig(JpaUserDetailService userDetailService) {
                this.userDetailService = userDetailService;
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(withDefaults())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**")
                                                .disable())
                                .headers(header -> header
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                                .formLogin(form -> form.disable())
                                .logout(out -> out
                                                .logoutUrl(endpoint + "/logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("h2-console/**").permitAll()
                                                .requestMatchers("/public").permitAll()
                                                .requestMatchers(HttpMethod.POST, endpoint + "/register").permitAll()
                                                .requestMatchers(HttpMethod.GET, endpoint + "/login").permitAll()
                                                .anyRequest().authenticated())
                                .userDetailsService(userDetailService)
                                .httpBasic(withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

                return http.build();
        }

}
