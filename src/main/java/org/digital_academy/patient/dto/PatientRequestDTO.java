package org.digital_academy.patient.dto;

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
    private String petIdentification;
    private String tutorName;
    private String tutorDni;
    private String tutorPhone;
    private String tutorEmail;
}
