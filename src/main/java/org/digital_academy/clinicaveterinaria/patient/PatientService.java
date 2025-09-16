package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> listarpatients() {
        return patientRepository.findAll();
    }

    public Patient guardarpatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> buscarPorId(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> buscarPorIdentificacion(String numeroIdentificacion) {
        return patientRepository.findByNumeroIdentificacion(numeroIdentificacion);
    }

    public void eliminarpatient(Long id) {
        patientRepository.deleteById(id);
    }
}
