package org.digital_academy.appointment;

import lombok.RequiredArgsConstructor;
import org.digital_academy.appointment.dto.AppointmentRequestDto;
import org.digital_academy.util.ApiMessageDto;
import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<ApiMessageDto> createAppointment(@RequestBody AppointmentRequestDto request) {
      
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = Appointment.builder()
                .appointmentDatetime(request.getAppointmentDatetime()) 
                .type(request.getType())                                
                .reason(request.getReason())                           
                .status("PENDING")                                      
                .patient(patient)
                .build();

        appointmentService.createAppointment(appointment);

        return ResponseEntity.ok(new ApiMessageDto("Appointment created and email sent"));
    }
}
