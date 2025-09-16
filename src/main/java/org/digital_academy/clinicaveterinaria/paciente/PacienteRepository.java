package org.digital_academy.clinicaveterinaria.paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Paciente> findByTelefonoTutor(String telefonoTutor);
}
