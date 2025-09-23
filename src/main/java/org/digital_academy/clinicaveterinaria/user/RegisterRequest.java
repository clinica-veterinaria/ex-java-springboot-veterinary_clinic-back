package org.digital_academy.clinicaveterinaria.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // "ADMIN" o "USER"
}
