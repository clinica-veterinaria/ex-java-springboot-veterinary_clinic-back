package org.digital_academy.treatment;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.digital_academy.config.SecurityConfig;
import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.digital_academy.treatment.dto.TreatmentRequestDTO;
import org.digital_academy.treatment.dto.TreatmentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TreatmentController.class)
@Import(SecurityConfig.class)
class TreatmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TreatmentRepository treatmentRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private TreatmentMapper treatmentMapper;

    private Patient patient;
    private TreatmentRequestDTO requestDTO;
    private TreatmentResponseDTO responseDTO;
    private Treatment treatment;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().id(1L).build();

        requestDTO = new TreatmentRequestDTO();
        requestDTO.setPatientId(1L);
        requestDTO.setTreatment("Vacunación");
        requestDTO.setMedication("Rabia");
        requestDTO.setDosage(1.0);
        requestDTO.setTreatmentDate(LocalDateTime.of(2025, 10, 1, 10, 0));

        treatment = Treatment.builder()
                .id(100L)
                .patient(patient)
                .treatment("Vacunación")
                .medication("Rabia")
                .dosage(1.0)
                .treatmentDate(LocalDateTime.of(2025, 10, 1, 10, 0))
                .build();

        responseDTO = new TreatmentResponseDTO(
                100L,
                null, // можна вставити маппінг PatientResponseDTO, якщо потрібно
                "Vacunación",
                "Rabia",
                1.0,
                LocalDateTime.of(2025, 10, 1, 10, 0));
    }

    @Test
    void createTreatment_ShouldReturnOk() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(treatmentMapper.toEntity(requestDTO, patient)).thenReturn(treatment);
        when(treatmentRepository.save(treatment)).thenReturn(treatment);
        when(treatmentMapper.toResponseDTO(treatment)).thenReturn(responseDTO);

        mockMvc.perform(post("/treatments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.treatment").value("Vacunación"))
                .andExpect(jsonPath("$.medication").value("Rabia"))
                .andExpect(jsonPath("$.dosage").value(1.0));
    }

    @Test
    void getTreatmentsByPatient_ShouldReturnList() throws Exception {
        List<Treatment> treatments = Arrays.asList(treatment);
        List<TreatmentResponseDTO> responses = Arrays.asList(responseDTO);

        when(treatmentRepository.findByPatientId(1L)).thenReturn(treatments);
        when(treatmentMapper.toResponseDTO(treatment)).thenReturn(responseDTO);

        mockMvc.perform(get("/treatments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].treatment").value("Vacunación"));
    }

    @Test
    void getAllTreatments_ShouldReturnList() throws Exception {
        List<Treatment> treatments = Arrays.asList(treatment);

        when(treatmentRepository.findAll()).thenReturn(treatments);
        when(treatmentMapper.toResponseDTO(treatment)).thenReturn(responseDTO);

        mockMvc.perform(get("/treatments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].treatment").value("Vacunación"));
    }
}
