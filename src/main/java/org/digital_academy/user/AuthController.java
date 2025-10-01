package org.digital_academy.user;

import org.digital_academy.user.dto.UserRequestDTO;
import org.digital_academy.user.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping(value = "/register", consumes = { "multipart/form-data" })
    public ResponseEntity<?> register(
            @RequestPart("userData") String userDataJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDTO request = objectMapper.readValue(userDataJson, UserRequestDTO.class);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ El correo electrónico ya está registrado.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Guardar roles CON prefijo ROLE_ en la BD
        if ("margarita@oliwa.com".equalsIgnoreCase(request.getEmail())) {
            user.setRoles(Collections.singleton("ROLE_ADMIN"));
        } else {
            user.setRoles(Collections.singleton("ROLE_USER"));
        }

        user.setName(request.getName());
        user.setDni(request.getDni());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (photoFile != null && !photoFile.isEmpty()) {
            user.setPhoto(photoFile.getBytes());
        }

        userRepository.save(user);
        return ResponseEntity.ok("✅ Usuario registrado con éxito");
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
    try {
        String email = credentials.get("email");
        String password = credentials.get("password");

        System.out.println("========== LOGIN ATTEMPT ==========");
        System.out.println("Email: " + email);

        // Crear el token de autenticación
        UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken(email, password);
        
        // Autenticar
        Authentication authentication = authenticationManager.authenticate(authToken);
        
        // Establecer en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Crear sesión manualmente
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        System.out.println("✅ Session ID: " + session.getId());
        System.out.println("✅ Authentication successful!");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserResponseDTO userDTO = UserResponseDTO.fromEntity(user);
        String roleWithPrefix = user.getRoles().iterator().next();
        String roleWithoutPrefix = roleWithPrefix.replace("ROLE_", "");

        return ResponseEntity.ok(Map.of(
                "user", userDTO,
                "role", roleWithoutPrefix));
    } catch (BadCredentialsException e) {
        System.out.println("❌ Bad credentials!");
        return ResponseEntity.status(401)
                .body(Map.of("error", "Usuario o contraseña incorrectos"));
    }
}

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }

    @GetMapping("/debug/check-user")
public ResponseEntity<?> checkUser(@RequestParam String email) {
    Optional<UserEntity> user = userRepository.findByEmail(email);
    
    if (user.isEmpty()) {
        return ResponseEntity.ok(Map.of("found", false, "email", email));
    }
    
    UserEntity u = user.get();
    return ResponseEntity.ok(Map.of(
        "found", true,
        "email", u.getEmail(),
        "username", u.getUsername(),
        "roles", u.getRoles(),
        "passwordExists", u.getPassword() != null,
        "passwordLength", u.getPassword().length()
    ));
}
}