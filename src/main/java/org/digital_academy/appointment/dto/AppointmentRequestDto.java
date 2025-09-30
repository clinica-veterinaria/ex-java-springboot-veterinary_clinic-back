package org.digital_academy.appointment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppointmentRequestDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date/time is required")
    @FutureOrPresent(message = "Appointment must be in the future")
    private LocalDateTime appointmentDatetime;

    @NotNull(message = "Type is required")
    @Pattern(regexp = "STANDARD|URGENT", message = "Type must be STANDARD or URGENT")
    private String type;

    @Pattern(regexp = "PENDING|ATTENDED|MISSED", message = "Status must be PENDING, ATTENDED, or MISSED")
    private String status;

    private String reason;
}