package org.digital_academy.appointment;

import org.digital_academy.appointment.dto.AppointmentResponseDto;
import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.digital_academy.util.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
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
    void createAppointment_Success() throws MessagingException {
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(appointment);

        assertNotNull(result);
        verify(emailService, times(1))
                .sendAppointmentConfirmation(eq(patient.getTutorEmail()),
                        eq(patient.getTutorName()),
                        eq(patient.getName()),
                        any(LocalDateTime.class));
    }

    @Test
    void createAppointment_InvalidType_Throws() {
        appointment.setType("WRONG");
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.createAppointment(appointment));
        assertEquals("Appointment type must be STANDARD or URGENT", ex.getMessage());
    }

    @Test
    void createAppointment_InvalidStatus_Throws() {
        appointment.setStatus("WRONG");
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.createAppointment(appointment));
        assertEquals("Invalid appointment status", ex.getMessage());
    }

    @Test
    void createAppointment_InPast_Throws() {
        appointment.setAppointmentDatetime(LocalDateTime.now().minusDays(1));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.createAppointment(appointment));
        assertEquals("Appointments cannot be scheduled in the past", ex.getMessage());
    }

    // ----------------- UPDATE -----------------
    @Test
    void updateAppointment_Success() throws MessagingException {
        Appointment updated = Appointment.builder()
                .patient(patient)
                .appointmentDatetime(LocalDateTime.now().plusDays(2))
                .type("URGENT")
                .status("PENDING")
                .reason("Vaccination")
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any())).thenReturn(updated);

        Appointment result = appointmentService.updateAppointment(1L, updated);

        assertEquals("URGENT", result.getType());
        verify(emailService, times(1))
                .sendAppointmentUpdate(eq(patient.getTutorEmail()),
                        eq(patient.getTutorName()),
                        eq(patient.getName()),
                        any(LocalDateTime.class));
    }

    @Test
    void updateAppointment_NotFound_Throws() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.updateAppointment(1L, appointment));
        assertEquals("Appointment not found", ex.getMessage());
    }

    // ----------------- CANCEL -----------------
    @Test
    void cancelAppointment_Success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        boolean canceled = appointmentService.cancelAppointment(1L);

        assertTrue(canceled);
        try {
            verify(emailService, times(1))
                    .sendAppointmentCancellation(eq(patient.getTutorEmail()),
                            eq(patient.getTutorName()),
                            eq(patient.getName()),
                            any(LocalDateTime.class));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void cancelAppointment_NotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        boolean canceled = appointmentService.cancelAppointment(1L);

        assertFalse(canceled);
        try {
            verify(emailService, never()).sendAppointmentCancellation(any(), any(), any(), any());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ----------------- markMissedAppointments -----------------
    @Test
    void markMissedAppointments_MarksPending() {
        Appointment past = Appointment.builder()
                .id(2L)
                .patient(patient)
                .appointmentDatetime(LocalDateTime.now().minusDays(1))
                .status("PENDING")
                .build();

        when(appointmentRepository.findByAppointmentDatetimeBefore(any())).thenReturn(List.of(past));
        when(appointmentRepository.save(any())).thenReturn(past);

        appointmentService.markMissedAppointments();

        assertEquals("MISSED", past.getStatus());
        verify(appointmentRepository, times(1)).save(past);
    }

    // ----------------- mapToResponseDto -----------------
    @Test
    void mapToResponseDto_Works() {
        AppointmentResponseDto dto = appointmentService.mapToResponseDto(appointment);

        assertEquals(appointment.getId(), dto.getId());
        assertEquals(appointment.getPatient().getName(), dto.getPatientName());
        assertEquals(appointment.getType(), dto.getType());
    }
}
