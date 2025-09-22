package org.digital_academy.appointment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {
    private Long patientId;                 
    private LocalDateTime appointmentDatetime; 
    private String type;                    
    private String reason;                  
}
