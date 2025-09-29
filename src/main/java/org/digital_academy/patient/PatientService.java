package org.digital_academy.patient;

import lombok.RequiredArgsConstructor;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;


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
    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO, MultipartFile imageFile) throws IOException {
        Patient patient = patientMapper.toEntity(requestDTO);
        String uniqueId = "PET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        patient.setPetIdentification(uniqueId); // Suponiendo que la entidad Patient tiene este setter

        if (imageFile != null && !imageFile.isEmpty()) {
            patient.setImage(imageFile.getBytes());
        }

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

    public List<PatientResponseDTO> searchPatients(String search, String species, String gender, String sortBy) {
        List<Patient> patients = patientRepository.searchWithFilters(search, species, gender, sortBy);
        return patients.stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
