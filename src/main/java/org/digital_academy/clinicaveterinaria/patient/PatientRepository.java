package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Patient> findByTelefonoTutor(String telefonoTutor);
}
