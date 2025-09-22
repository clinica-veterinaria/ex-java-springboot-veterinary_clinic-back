package org.digital_academy.appointment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponseDto {

    private Long id;
    private LocalDateTime appointmentDatetime;
    private String type;
    private String status;
    private String reason;

    private Long patientId;
    private String patientName;
    private String tutorName;
    private String tutorEmail;
}
