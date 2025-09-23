package org.digital_academy.patient;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDNI;
    private String ownerName;
    private String phone;
}
