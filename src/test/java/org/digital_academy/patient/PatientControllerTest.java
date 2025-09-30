package org.digital_academy.patient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.digital_academy.config.SecurityConfig;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    private PatientRequestDTO sampleRequest() {
        return PatientRequestDTO.builder()
                .name("Bobby")
                .age(3)
                .breed("Labrador")
                .gender("Male")
                .petIdentification("PET123")
                .tutorName("Alice Smith")
                .tutorDni("12345678A")
                .tutorPhone("600123456")
                .tutorEmail("alice@example.com")
                .build();
    }

    private PatientResponseDTO sampleResponse(Long id) {
        return PatientResponseDTO.builder()
                .id(id)
                .name("Bobby")
                .age(3)
                .breed("Labrador")
                .gender("Male")
                .petIdentification("PET123")
                .tutorName("Alice Smith")
                .tutorDni("12345678A")
                .tutorPhone("600123456")
                .tutorEmail("alice@example.com")
                .build();
    }

    @Test
    void testGetAllPatients() throws Exception {
        when(patientService.getAllPatients())
                .thenReturn(List.of(sampleResponse(1L), sampleResponse(2L)));

        mockMvc.perform(get("/patients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCreatePatient() throws Exception {
        when(patientService.createPatient(any(PatientRequestDTO.class)))
                .thenReturn(sampleResponse(1L));

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bobby"));
    }

    @Test
    void testGetPatientById_Found() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(get("/patients/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdatePatient() throws Exception {
        when(patientService.updatePatient(any(Long.class), any(PatientRequestDTO.class)))
                .thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(put("/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByPetIdentification() throws Exception {
        when(patientService.getByPetIdentification("PET123"))
                .thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(get("/patients/petIdentification/PET123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetByTutorDni() throws Exception {
        when(patientService.getByTutorDni("12345678A"))
                .thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(get("/patients/tutorDni/12345678A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetByTutorPhone() throws Exception {
        when(patientService.getByTutorPhone("600123456"))
                .thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(get("/patients/tutorPhone/600123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetByTutorEmail() throws Exception {
        when(patientService.getByTutorEmail("alice@example.com"))
                .thenReturn(Optional.of(sampleResponse(1L)));

        mockMvc.perform(get("/patients/tutorEmail/alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
