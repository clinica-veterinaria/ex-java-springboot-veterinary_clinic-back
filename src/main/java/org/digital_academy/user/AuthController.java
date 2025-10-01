package org.digital_academy.user;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest request;
        try {
            request = objectMapper.readValue(userDataJson, RegisterRequest.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("⚠️ Formato de datos de usuario inválido.");
        }

        // Usamos email como username
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
        user.setPhone(request.getPhone()); // unificado con DTO

        if (photoFile != null && !photoFile.isEmpty()) {
            user.setPhoto(photoFile.getBytes()); // unificado con DTO
        }

        userRepository.save(user);

        return ResponseEntity.ok("✅ Usuario registrado con éxito");
    }

    // ✅ Login con validación
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("===========================================");
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("📧 Email recibido: " + request.getEmail());
        System.out.println("🔑 Password recibida: " + request.getPassword());

    try {
       /* authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );*/ 

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ Usuario NO encontrado en BD");
                    return new RuntimeException("Usuario no encontrado");
                });

            System.out.println("✅ Usuario encontrado: " + user.getEmail());
            System.out.println("🔐 Password en BD (hash): " + user.getPassword());
            System.out.println("👤 Roles del usuario: " + user.getRoles());

            System.out.println("🔄 Intentando autenticar con AuthenticationManager...");
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        
        System.out.println("✅✅✅ Autenticación EXITOSA!");

        String role = user.getRoles().isEmpty() 
            ? "USER" 
            : user.getRoles().iterator().next().toString();
        
        System.out.println("🎯 Role devuelto: " + role);
        System.out.println("===========================================");

        return ResponseEntity.ok(Map.of(
            "message", "✅ Login exitoso",
            "role", role,
            "user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
            )
        ));
        
    } catch (BadCredentialsException e) {
        System.out.println("❌❌❌ BadCredentialsException!");
        System.out.println("Mensaje: " + e.getMessage());
        e.printStackTrace();
        System.out.println("===========================================");
        return ResponseEntity.status(401)
            .body(Map.of("error", "Usuario o contraseña incorrectos"));
    } catch (Exception e) {
        System.out.println("❌❌❌ Exception general!");
        System.out.println("Tipo: " + e.getClass().getName());
        System.out.println("Mensaje: " + e.getMessage());
        e.printStackTrace();
        System.out.println("===========================================");
        return ResponseEntity.status(401)
            .body(Map.of("error", "Usuario o contraseña incorrectos"));
    }
}

    @PostMapping("/test-hash")
    public ResponseEntity<?> testHash() {
        String plainPassword = "admin123";
        String hashed = passwordEncoder.encode(plainPassword);
        System.out.println("Password en texto plano: " + plainPassword);
        System.out.println("Password hasheada: " + hashed);
    
        String passwordEnBD = "$2a$10$7Q6c7A0VZyN1j8H8vwrNNOiKQqT7HcbMOnm.6Zbsz4PzZQGd1hY8e";
        boolean matches = passwordEncoder.matches(plainPassword, passwordEnBD);

        System.out.println("¿'admin123' coincide con el hash en BD? " + matches);
    
        return ResponseEntity.ok(Map.of(
        "passwordPlana", plainPassword,
        "hashNuevo", hashed,
        "hashEnBD", passwordEnBD,
        "coincide", matches
    ));
}
}

 /*       // Obtener el primer rol
        String role = user.getRoles().isEmpty() 
            ? "USER" 
            : user.getRoles().iterator().next().toString();

        return ResponseEntity.ok(Map.of(
            "message", "✅ Login exitoso",
            "role", role,
            "user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
            )
        ));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(401)
            .body(Map.of("error", "Usuario o contraseña incorrectos"));
    }
}
    // ✅ Logout (stateless)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }*/
