package org.digital_academy.patient.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {
    private Long id;       // 👈 Incluimos el id generado
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDNI;
    private String ownerName;
    private String phone;
}
