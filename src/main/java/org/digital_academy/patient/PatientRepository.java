package org.digital_academy.patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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
}
