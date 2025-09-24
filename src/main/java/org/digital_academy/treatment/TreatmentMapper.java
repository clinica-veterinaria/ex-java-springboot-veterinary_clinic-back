package org.digital_academy.treatment;

import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientMapper;
import org.digital_academy.treatment.dto.TreatmentRequestDTO;
import org.digital_academy.treatment.dto.TreatmentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TreatmentMapper {

    private final PatientMapper patientMapper;

    public TreatmentMapper(PatientMapper patientMapper) {
        this.patientMapper = patientMapper;
    }

    // De entity a response DTO
    public TreatmentResponseDTO toResponseDTO(Treatment treatment) {
        return new TreatmentResponseDTO(
            treatment.getId(),
            patientMapper.toResponseDTO(treatment.getPatient()),
            treatment.getTreatment(),
            treatment.getMedication(),
            treatment.getDosage(),
            treatment.getTreatmentDate()
        );
    }

    // De request DTO a entity
    public Treatment toEntity(TreatmentRequestDTO dto, Patient patient) {
        return Treatment.builder()
                .patient(patient)
                .treatment(dto.getTreatment())
                .medication(dto.getMedication())
                .dosage(dto.getDosage())
                .treatmentDate(dto.getTreatmentDate())
                .build();
    }
}
