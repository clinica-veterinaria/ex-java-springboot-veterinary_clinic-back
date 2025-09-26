package org.digital_academy.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
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
    @PostMapping(value = "/register", consumes = { "multipart/form-data" })
    public ResponseEntity<?> register(
            @RequestPart("userData") String userDataJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest request;
        try {
            request = objectMapper.readValue(userDataJson, RegisterRequest.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("⚠️ Formato de datos de usuario inválido.");
        }

        request.setUsername(request.getEmail());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ El correo electrónico ya está registrado.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("⚠️ La contraseña no puede ser vacía");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(Collections.singleton(request.getRole() != null ? request.getRole() : "USER"));

        user.setName(request.getName());
        user.setDni(request.getDni());
        user.setEmail(request.getEmail());
        user.setTelefono(request.getTelefono());

        if (imagen != null && !imagen.isEmpty()) {
            user.setImage(imagen.getBytes());
        }

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
                            request.getPassword()));
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
