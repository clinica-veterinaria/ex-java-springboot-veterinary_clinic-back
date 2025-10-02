package org.digital_academy.patient;

import jakarta.persistence.*;
import lombok.*;
import org.digital_academy.appointment.Appointment;
import org.digital_academy.treatment.Treatment;

import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    private String breed;

    private String gender;

    @Column(nullable = false, unique = true)
    private String petIdentification;

    @Column(nullable = false)
    private String tutorName;

    @Column(nullable = false)
    private String tutorDni;

    @Column(nullable = false)
    private String tutorPhone;

    @Column(nullable = false)
    private String tutorEmail;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    // Borrado en cascada de citas
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    // Borrado en cascada de tratamientos
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treatment> treatments;
}
