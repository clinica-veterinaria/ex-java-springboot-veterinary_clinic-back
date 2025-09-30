package org.digital_academy.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.digital_academy.appointment.dto.AppointmentResponseDto;
import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.digital_academy.util.EmailService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;
    private final PatientRepository patientRepository;

    public List<Appointment> searchAndFilterAppointments(
        String search, 
        String type, 
        String status, 
        String sortBy
    ) {
        
        List<Appointment> appointments = appointmentRepository.searchWithFilters(
            search, 
            type, 
            status, 
            sortBy 
        );

       
        if (sortBy != null && sortBy.equalsIgnoreCase("fecha")) {
            appointments.sort(
                (a1, a2) -> {
                    return a1.getAppointmentDatetime().compareTo(a2.getAppointmentDatetime());
                }
            );
        }

        return appointments;
    }
    

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getUpcomingAppointments(int limit) {
        return appointmentRepository.findByAppointmentDatetimeAfterOrderByAppointmentDatetimeAsc(
            LocalDateTime.now(),
            PageRequest.of(0, limit)
        );
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByAppointmentDatetimeBetween(startOfDay, endOfDay);
    }

    public List<String> getAvailableSlotsForDate(LocalDate date) {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        int slotDurationMinutes = 20;

        List<String> allSlots = new ArrayList<>();
        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            allSlots.add(currentTime.toString());
            currentTime = currentTime.plusMinutes(slotDurationMinutes);
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<Appointment> existingAppointments = appointmentRepository.findByAppointmentDatetimeBetween(startOfDay, endOfDay);

        List<String> occupiedSlots = existingAppointments.stream()
                .map(a -> a.getAppointmentDatetime().toLocalTime().toString())
                .collect(Collectors.toList());

        return allSlots.stream()
                .filter(slot -> !occupiedSlots.contains(slot))
                .collect(Collectors.toList());
    }
    

    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);

        Appointment saved = appointmentRepository.save(appointment);
        sendEmailSafe(saved, EmailType.CONFIRMATION);
        return saved;
    }

 public Appointment updateAppointment(Long id, Appointment updated) {
    validateAppointment(updated);

    return appointmentRepository.findById(id).map(existing -> {
        existing.setAppointmentDatetime(updated.getAppointmentDatetime());
        existing.setType(updated.getType());
        existing.setReason(updated.getReason());
        existing.setStatus(updated.getStatus());

        Patient patient = patientRepository.findById(updated.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setPatient(patient);

        Appointment saved = appointmentRepository.save(existing);
        sendEmailSafe(saved, EmailType.UPDATE);
        return saved;
    }).orElseThrow(() -> new RuntimeException("Appointment not found"));
}


   public boolean cancelAppointment(Long id) {
    return appointmentRepository.findById(id).map(appointment -> {
        appointmentRepository.delete(appointment);
        sendEmailSafe(appointment, EmailType.CANCELLATION);
        return true;
    }).orElse(false);
}

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private void validateAppointment(Appointment appointment) {
        LocalDateTime dateTime = appointment.getAppointmentDatetime();

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointments cannot be scheduled in the past");
        }

        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        int count = appointmentRepository.findByAppointmentDatetimeBetween(startOfDay, endOfDay).size();
        if (count >= 10) {
            throw new RuntimeException("No more than 10 appointments allowed per day");
        }

        
        if (!appointment.getType().equalsIgnoreCase("STANDARD")
                && !appointment.getType().equalsIgnoreCase("URGENT")) {
            throw new RuntimeException("Appointment type must be STANDARD or URGENT");
        }

    
        if (!appointment.getStatus().equalsIgnoreCase("PENDING")
                && !appointment.getStatus().equalsIgnoreCase("ATTENDED")
                && !appointment.getStatus().equalsIgnoreCase("MISSED")) {
            throw new RuntimeException("Invalid appointment status");
        }
    }

    private enum EmailType { CONFIRMATION, UPDATE, CANCELLATION }

    private void sendEmailSafe(Appointment appointment, EmailType type) {
        Patient patient = appointment.getPatient();
        if (patient != null && patient.getTutorEmail() != null && appointment.getAppointmentDatetime() != null) {
            try {
                switch (type) {
                    case CONFIRMATION -> emailService.sendAppointmentConfirmation(
                            patient.getTutorEmail(),
                            patient.getTutorName(),
                            patient.getName(),
                            appointment.getAppointmentDatetime()
                    );
                    case UPDATE -> emailService.sendAppointmentUpdate(
                            patient.getTutorEmail(),
                            patient.getTutorName(),
                            patient.getName(),
                            appointment.getAppointmentDatetime()
                    );
                    case CANCELLATION -> emailService.sendAppointmentCancellation(
                            patient.getTutorEmail(),
                            patient.getTutorName(),
                            patient.getName(),
                            appointment.getAppointmentDatetime()
                    );
                }
                log.info("Email {} sent to {}", type, patient.getTutorEmail());
            } catch (MessagingException e) {
                log.error("Error sending {} email", type, e);
            }
        } else {
            log.warn("Cannot send email: incomplete data (email/datetime)");
        }
    }

    
    @Scheduled(cron = "0 0 * * * *") 
    public void markMissedAppointments() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> pastAppointments = appointmentRepository.findByAppointmentDatetimeBefore(now);

        pastAppointments.stream()
                .filter(a -> a.getStatus().equalsIgnoreCase("PENDING"))
                .forEach(a -> {
                    a.setStatus("MISSED");
                    appointmentRepository.save(a);
                    log.info("Appointment {} marked as MISSED", a.getId());
                });
    }

    public AppointmentResponseDto mapToResponseDto(Appointment appointment) {
    return AppointmentResponseDto.builder()
            .id(appointment.getId())
            .appointmentDatetime(appointment.getAppointmentDatetime())
            .type(appointment.getType())
            .status(appointment.getStatus())
            .reason(appointment.getReason())
            .patientId(appointment.getPatient().getId())
            .patientName(appointment.getPatient().getName())
            .tutorName(appointment.getPatient().getTutorName())
            .tutorEmail(appointment.getPatient().getTutorEmail())
            .build();
}



}