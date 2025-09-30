package org.digital_academy.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByAppointmentDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByAppointmentDatetimeBefore(LocalDateTime dateTime);

    List<Appointment> findByAppointmentDatetimeAfterOrderByAppointmentDatetimeAsc(LocalDateTime dateTime, Pageable pageable);
    

    @Query("SELECT a FROM Appointment a LEFT JOIN a.patient p WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.tutorName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.reason) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:type IS NULL OR :type = '' OR a.type = :type) " +
            "AND (:status IS NULL OR :status = '' OR a.status = :status)")
    List<Appointment> searchWithFilters(
            @Param("search") String search,
            @Param("type") String type,
            @Param("status") String status,
            @Param("sortBy") String sortBy);

}
