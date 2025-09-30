package org.digital_academy.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByAppointmentDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByAppointmentDatetimeBefore(LocalDateTime dateTime);

    @Query("SELECT a FROM Appointment a LEFT JOIN a.patient p WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.tutorName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.reason) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:type IS NULL OR :type = '' OR LOWER(a.type) = LOWER(:type)) " +
           "AND (:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status)) " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'fecha' THEN a.appointmentDatetime END ASC, " +
           "a.id DESC")
    List<Appointment> searchWithFilters(
        @Param("search") String search,
        @Param("type") String type,
        @Param("status") String status,
        @Param("sortBy") String sortBy
    );
    
}
