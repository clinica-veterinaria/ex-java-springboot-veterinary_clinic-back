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
           // Búsqueda general en múltiples campos
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(CONCAT(p.age, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.breed) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.gender) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.petIdentification) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.tutorName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.tutorDni) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.tutorPhone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.tutorEmail) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           // Filtros específicos
           "AND (:species IS NULL OR :species = '' OR LOWER(p.species) = LOWER(:species)) " +
           "AND (:gender IS NULL OR :gender = '' OR LOWER(p.gender) = LOWER(:gender)) " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'fecha' THEN p.id END DESC, " +
           "p.id DESC")
    List<Patient> searchWithFilters(
        @Param("search") String search,
        @Param("species") String species,
        @Param("gender") String gender,
        @Param("sortBy") String sortBy
    );
}
