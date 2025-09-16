package org.digital_academy.patient;

import jakarta.persistence.*;
import lombok.*;
import org.digital_academy.appointment.Appointment;
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
    private String nombre;

    @Column(nullable = false)
    private Integer edad;

    private String raza;

    private String genero;

    @Column(nullable = false, unique = true)
    private String identificacion;

    @Column(nullable = false)
    private String tutorNombre;

    @Column(nullable = false)
    private String tutorTelefono;

    @Lob
    @Column(name = "image")
    private byte[] image; 

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}
