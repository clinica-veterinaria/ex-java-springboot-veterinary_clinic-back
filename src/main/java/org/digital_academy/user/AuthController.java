package org.digital_academy.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Registro de usuarios
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ El usuario ya existe");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("⚠️ La contraseña no puede ser vacía");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(request.getRole())); // asigna un rol

        userRepository.save(user);

        return ResponseEntity.ok("✅ Usuario registrado con éxito");
    }

    // ✅ Login con validación
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            return ResponseEntity.ok(Map.of("message", "✅ Login exitoso"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("❌ Usuario o contraseña incorrectos");
        }
    }

    // ✅ Logout (stateless, solo para frontend o para limpiar cookies/sesión)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Si usas JWT, el logout es responsabilidad del frontend (borrar token)
        // Aquí solo devuelves un mensaje de éxito
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }
}
