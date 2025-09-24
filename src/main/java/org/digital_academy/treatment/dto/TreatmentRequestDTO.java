package org.digital_academy.treatment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRequestDTO {
    private Long patientId;       
    private String treatment;
    private String medication;
    private Double dosage;
    private LocalDateTime treatmentDate;
}
