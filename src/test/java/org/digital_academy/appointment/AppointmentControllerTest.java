package org.digital_academy.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    private MockMvc mockMvc;

    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        patient = Patient.builder()
                .id(1L)
                .name("Fido")
                .tutorName("John Doe")
                .tutorEmail("john@example.com")
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .patient(patient)
                .appointmentDatetime(LocalDateTime.now().plusDays(1))
                .type("STANDARD")
                .status("PENDING")
                .reason("Checkup")
                .build();
    }

    // ----------------- CREATE -----------------
    @Test
    void createAppointment_Success() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentService.createAppointment(any())).thenReturn(appointment);
        when(appointmentService.mapToResponseDto(any())).thenCallRealMethod();

        String json = """
                {
                    "patientId": 1,
                    "appointmentDatetime": "%s",
                    "type": "STANDARD",
                    "reason": "Checkup"
                }
                """.formatted(appointment.getAppointmentDatetime());

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientName").value("Fido"))
                .andExpect(jsonPath("$.type").value("STANDARD"));
    }

    // ----------------- GET ALL -----------------
    @Test
    void getAllAppointments_Success() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(appointment));
        when(appointmentService.mapToResponseDto(any())).thenCallRealMethod();

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ----------------- GET BY ID -----------------
    @Test
    void getAppointmentById_Found() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentService.mapToResponseDto(any())).thenCallRealMethod();

        mockMvc.perform(get("/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAppointmentById_NotFound() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/appointments/1"))
                .andExpect(status().isNotFound());
    }

    // ----------------- GET BY PATIENT -----------------
    @Test
    void getAppointmentsByPatient_Success() throws Exception {
        when(appointmentService.getAppointmentsByPatient(1L)).thenReturn(List.of(appointment));
        when(appointmentService.mapToResponseDto(any())).thenCallRealMethod();

        mockMvc.perform(get("/appointments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ----------------- UPDATE -----------------
    @Test
    void updateAppointment_Success() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentService.updateAppointment(eq(1L), any())).thenReturn(appointment);
        when(appointmentService.mapToResponseDto(any())).thenCallRealMethod();

        String json = """
                {
                    "patientId": 1,
                    "appointmentDatetime": "%s",
                    "type": "STANDARD",
                    "reason": "Checkup",
                    "status": "PENDING"
                }
                """.formatted(appointment.getAppointmentDatetime());

        mockMvc.perform(put("/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ----------------- CANCEL -----------------
    @Test
    void cancelAppointment_Success() throws Exception {
        when(appointmentService.cancelAppointment(1L)).thenReturn(true);

        mockMvc.perform(delete("/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment cancelled and email sent"));
    }

    @Test
    void cancelAppointment_NotFound() throws Exception {
        when(appointmentService.cancelAppointment(1L)).thenReturn(false);

        mockMvc.perform(delete("/appointments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Appointment not found"));
    }
}
