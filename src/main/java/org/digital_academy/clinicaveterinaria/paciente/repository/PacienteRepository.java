package org.digital_academy.clinicaveterinaria.paciente.repository;

import org.digital_academy.clinicaveterinaria.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Paciente> findByTelefonoTutor(String telefonoTutor);
}
