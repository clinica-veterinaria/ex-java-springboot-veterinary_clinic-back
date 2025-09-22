package org.digital_academy.clinicaveterinaria.patient;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDNI;

    private String ownerName;
    private String phone;
}
