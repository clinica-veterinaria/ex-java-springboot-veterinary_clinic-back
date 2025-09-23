// package org.digital_academy.clinicaveterinaria.user;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class UserDetailsConfig {

//     private final UserRepository userRepository;

//     public UserDetailsConfig(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     @Bean
//     public UserDetailsService userDetailsService() {
//         return username -> {
//             UserEntity user = userRepository.findByUsername(username)
//                     .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

//             return User.builder()
//                     .username(user.getUsername())
//                     .password(user.getPassword())
//                     .roles(user.getRoles().toArray(new String[0]))
//                     .build();
//         };
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder(); // 👈 para guardar contraseñas encriptadas
//     }
// }