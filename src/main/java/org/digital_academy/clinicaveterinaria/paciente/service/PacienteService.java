package org.digital_academy.clinicaveterinaria.paciente.service;

import org.digital_academy.clinicaveterinaria.paciente.model.Paciente;
import org.digital_academy.clinicaveterinaria.paciente.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public Optional<Paciente> buscarPorIdentificacion(String numeroIdentificacion) {
        return pacienteRepository.findByNumeroIdentificacion(numeroIdentificacion);
    }

    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
}
