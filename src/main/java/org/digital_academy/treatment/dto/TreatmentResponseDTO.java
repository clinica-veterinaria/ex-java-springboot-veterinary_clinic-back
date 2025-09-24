package org.digital_academy.treatment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.digital_academy.patient.dto.PatientResponseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentResponseDTO {
    private Long id;
    private PatientResponseDTO patient;
    private String treatment;
    private String medication;
    private Double dosage;
    private LocalDateTime treatmentDate;
}
