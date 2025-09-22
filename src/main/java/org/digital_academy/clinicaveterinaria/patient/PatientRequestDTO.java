package org.digital_academy.clinicaveterinaria.patient;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {
    private String name;
    private String gender;
    private String breed;
    private String age;
    private String ownerName;
    private String ownerDNI;
    private String phone;
    private String email;
}
