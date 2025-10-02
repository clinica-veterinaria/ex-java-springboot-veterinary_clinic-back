package org.digital_academy.treatment;

import jakarta.persistence.*;
import lombok.*;
import org.digital_academy.patient.Patient;

import java.time.LocalDateTime;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "treatment", nullable = false)
    private String treatment;

    @Column(name = "medication")
    private String medication;

    @Column(name = "dosage")
    private Double dosage;

    @Column(name = "treatment_date", nullable = false)
    private LocalDateTime treatmentDate;
}
