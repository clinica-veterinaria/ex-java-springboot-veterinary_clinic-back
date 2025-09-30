package org.digital_academy.treatment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientMapper;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.treatment.dto.TreatmentRequestDTO;
import org.digital_academy.treatment.dto.TreatmentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

class TreatmentMapperTest {

    private PatientMapper patientMapper;
    private TreatmentMapper treatmentMapper;

    @BeforeEach
    void setUp() {
        patientMapper = mock(PatientMapper.class);
        treatmentMapper = new TreatmentMapper(patientMapper);
    }

    @Test
    void toResponseDTO_ShouldMapTreatmentToTreatmentResponseDTO() {
        Patient patient = Patient.builder().id(1L).build();
        Treatment treatment = Treatment.builder()
                .id(100L)
                .patient(patient)
                .treatment("Vacunación")
                .medication("Rabia")
                .dosage(1.0)
                .treatmentDate(LocalDateTime.of(2025, 10, 1, 10, 0))
                .build();

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(1L);

        when(patientMapper.toResponseDTO(patient)).thenReturn(patientResponseDTO);

        TreatmentResponseDTO responseDTO = treatmentMapper.toResponseDTO(treatment);

        assertEquals(100L, responseDTO.getId());
        assertEquals(patientResponseDTO, responseDTO.getPatient());
        assertEquals("Vacunación", responseDTO.getTreatment());
        assertEquals("Rabia", responseDTO.getMedication());
        assertEquals(1.0, responseDTO.getDosage());
        assertEquals(LocalDateTime.of(2025, 10, 1, 10, 0), responseDTO.getTreatmentDate());

        verify(patientMapper).toResponseDTO(patient);
    }

    @Test
    void toEntity_ShouldMapTreatmentRequestDTOAndPatientToTreatment() {
        Patient patient = Patient.builder().id(1L).build();
        TreatmentRequestDTO requestDTO = new TreatmentRequestDTO();
        requestDTO.setTreatment("Vacunación");
        requestDTO.setMedication("Rabia");
        requestDTO.setDosage(1.0);
        requestDTO.setTreatmentDate(LocalDateTime.of(2025, 10, 1, 10, 0));

        Treatment treatment = treatmentMapper.toEntity(requestDTO, patient);

        assertEquals("Vacunación", treatment.getTreatment());
        assertEquals("Rabia", treatment.getMedication());
        assertEquals(1.0, treatment.getDosage());
        assertEquals(LocalDateTime.of(2025, 10, 1, 10, 0), treatment.getTreatmentDate());
        assertEquals(patient, treatment.getPatient());
    }
}
