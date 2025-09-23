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
    private String name;
    @Column(nullable = false)
    private Integer age;
    private String breed;
    private String gender;
    @Column(nullable = false, unique = true)
    private String petIdentification;
    @Column(nullable = false)
    private String tutorName;
    @Column(nullable = false, unique = true)
    private String tutorDni;
    @Column(nullable = false)
    private String tutorPhone;
    @Column(nullable = false, unique = true)
    private String tutorEmail;
    @Lob
    @Column(name = "image")
    private byte[] image;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}