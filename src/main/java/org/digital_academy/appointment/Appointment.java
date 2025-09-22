package org.digital_academy.appointment;

import jakarta.persistence.*;
import lombok.*;
import org.digital_academy.patient.Patient;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointmentDatetime", nullable = false)
    private LocalDateTime appointmentDatetime;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}
