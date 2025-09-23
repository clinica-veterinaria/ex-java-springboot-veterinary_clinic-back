package org.digital_academy.clinicaveterinaria.auth;

import org.digital_academy.clinicaveterinaria.user.JpaUserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JpaUserDetailService userService;

    public AuthService(JpaUserDetailService userService) {
        this.userService = userService;
    }

    public ResponseEntity<String> register(RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<AuthDTOResponse> login() {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String authority = auth.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority())
                .orElse("NO_ROLE");
        AuthDTOResponse authResponse = new AuthDTOResponse("Logged", auth.getName(), authority);
        return ResponseEntity.accepted().body(authResponse);
    }
}
