package org.digital_academy.clinicaveterinaria.config;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ejemplo: ROLE_ADMIN, ROLE_VETERINARIO, ROLE_RECEPCIONISTA
}