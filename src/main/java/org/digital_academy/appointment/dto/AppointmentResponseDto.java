package org.digital_academy.appointment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentResponseDto {

    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDatetime;
    private String type;
    private String status;
    private String reason;

    private Long patientId;
    private String patientName;
    private String tutorName;
    private String tutorEmail;
}
