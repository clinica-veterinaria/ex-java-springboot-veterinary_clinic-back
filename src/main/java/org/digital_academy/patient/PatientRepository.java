package org.digital_academy.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Buscar por identificación del paciente
    Optional<Patient> findByPetIdentification(String petIdentification);

    // Buscar por DNI del tutor
    Optional<Patient> findByTutorDni(String tutorDni);

    // Buscar por email del tutor
    Optional<Patient> findByTutorEmail(String tutorEmail);

    // Buscar por teléfono del tutor
    Optional<Patient> findByTutorPhone(String tutorPhone);

    // Buscar por nombre del paciente
    Optional<Patient> findByName(String name);

    @Query("SELECT p FROM Patient p WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(CAST(p.age AS string)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.breed) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.gender) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.petIdentification) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.tutorName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.tutorDni) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.tutorPhone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.tutorEmail) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:breed IS NULL OR :breed = '' OR p.breed = :breed) " +
            "AND (:gender IS NULL OR :gender = '' OR p.gender = :gender)")
    List<Patient> searchWithFilters(
            @Param("search") String search,
            @Param("breed") String breed,
            @Param("gender") String gender,
            @Param("sortBy") String sortBy
        );
}
