package org.digital_academy.appointment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.digital_academy.appointment.dto.AppointmentRequestDto;
import org.digital_academy.appointment.dto.AppointmentResponseDto;
import org.digital_academy.util.ApiMessageDto;
import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

   @PostMapping
public ResponseEntity<AppointmentResponseDto> createAppointment(
        @Valid @RequestBody AppointmentRequestDto request) {

    Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));

    Appointment appointment = Appointment.builder()
            .appointmentDatetime(request.getAppointmentDatetime())
            .type(request.getType())
            .reason(request.getReason())
            .status("PENDING")
            .patient(patient)
            .build();

    Appointment saved = appointmentService.createAppointment(appointment);
    AppointmentResponseDto response = appointmentService.mapToResponseDto(saved);

    return ResponseEntity.status(201).body(response);
}


  @GetMapping
public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
    List<AppointmentResponseDto> response = appointmentService.getAllAppointments().stream()
            .map(appointmentService::mapToResponseDto)
            .toList();
    return ResponseEntity.ok(response);
}

  @GetMapping("/{id}")
public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
    return appointmentService.getAppointmentById(id)
            .map(appointmentService::mapToResponseDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

@GetMapping("/patient/{patientId}")
public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByPatient(@PathVariable Long patientId) {
    List<AppointmentResponseDto> response = appointmentService.getAppointmentsByPatient(patientId).stream()
            .map(appointmentService::mapToResponseDto)
            .toList();
    return ResponseEntity.ok(response);
}

   @PutMapping("/{id}")
public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable Long id,
        @Valid @RequestBody AppointmentRequestDto request) {

    Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));

    Appointment updated = Appointment.builder()
            .appointmentDatetime(request.getAppointmentDatetime())
            .type(request.getType())
            .reason(request.getReason())
            .status(request.getStatus() != null ? request.getStatus() : "PENDING")
            .patient(patient)
            .build();

    Appointment saved = appointmentService.updateAppointment(id, updated);
    return ResponseEntity.ok(appointmentService.mapToResponseDto(saved));
}


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageDto> cancelAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.cancelAppointment(id);
        if (!deleted) {
            return ResponseEntity.status(404).body(new ApiMessageDto("Appointment not found"));
        }
        return ResponseEntity.ok(new ApiMessageDto("Appointment cancelled and email sent"));
    }
}
