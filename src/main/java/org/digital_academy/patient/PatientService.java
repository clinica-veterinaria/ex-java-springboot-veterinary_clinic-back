package org.digital_academy.patient;

import lombok.RequiredArgsConstructor;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    // Listar todos los pacientes
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener paciente por ID
    public Optional<PatientResponseDTO> getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toResponseDTO);
    }

    // Crear paciente
    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        Patient patient = patientMapper.toEntity(requestDTO);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponseDTO(saved);
    }

    // Actualizar paciente
    public Optional<PatientResponseDTO> updatePatient(Long id, PatientRequestDTO requestDTO) {
        return patientRepository.findById(id)
                .map(existing -> {
                    patientMapper.updateEntityFromDto(requestDTO, existing);
                    Patient updated = patientRepository.save(existing);
                    return patientMapper.toResponseDTO(updated);
                });
    }

    // Eliminar paciente
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    // Buscar por identificación del paciente
    public Optional<PatientResponseDTO> getByPetIdentification(String petIdentification) {
        return patientRepository.findByPetIdentification(petIdentification)
                .map(patientMapper::toResponseDTO);
    }

    // Buscar por DNI del tutor
    public Optional<PatientResponseDTO> getByTutorDni(String tutorDni) {
        return patientRepository.findByTutorDni(tutorDni)
                .map(patientMapper::toResponseDTO);
    }

    // Buscar por teléfono del tutor
    public Optional<PatientResponseDTO> getByTutorPhone(String tutorPhone) {
        return patientRepository.findByTutorPhone(tutorPhone)
                .map(patientMapper::toResponseDTO);
    }

    // Buscar por email del tutor
    public Optional<PatientResponseDTO> getByTutorEmail(String tutorEmail) {
        return patientRepository.findByTutorEmail(tutorEmail)
                .map(patientMapper::toResponseDTO);
    }
}
