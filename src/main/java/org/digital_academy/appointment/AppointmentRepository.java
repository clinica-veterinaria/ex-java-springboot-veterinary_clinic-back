package org.digital_academy.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByAppointmentDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByAppointmentDatetimeBefore(LocalDateTime dateTime);

    List<Appointment> findByAppointmentDatetimeAfterOrderByAppointmentDatetimeAsc(LocalDateTime dateTime, Pageable pageable);
    
}
