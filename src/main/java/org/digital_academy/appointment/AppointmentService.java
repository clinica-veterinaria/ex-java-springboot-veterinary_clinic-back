package org.digital_academy.appointment;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.digital_academy.patient.Patient;
import org.digital_academy.util.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public Appointment createAppointment(Appointment appointment) {
        Appointment saved = appointmentRepository.save(appointment);
        sendEmailSafe(saved, EmailType.CONFIRMATION);
        return saved;
    }

    public Appointment updateAppointment(Long id, Appointment updated) {
        return appointmentRepository.findById(id).map(existing -> {
            existing.setAppointmentDatetime(updated.getAppointmentDatetime());
            existing.setType(updated.getType());
            existing.setReason(updated.getReason());
            existing.setStatus(updated.getStatus());
            Appointment saved = appointmentRepository.save(existing);
            sendEmailSafe(saved, EmailType.UPDATE);
            return saved;
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.findById(id).ifPresent(appointment -> {
            appointmentRepository.delete(appointment);
            sendEmailSafe(appointment, EmailType.CANCELLATION);
        });
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

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
