package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper mapper;

    public PatientService(PatientRepository patientRepository, PatientMapper mapper) {
        this.patientRepository = patientRepository;
        this.mapper = mapper;
    }

    // ✅ Listar todos los pacientes
    public List<PatientResponseDTO> listarPatients() {
        return patientRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ✅ Guardar un paciente nuevo
    public PatientResponseDTO guardarPatient(PatientRequestDTO requestDTO) {
        Patient patient = mapper.toEntity(requestDTO);
        Patient saved = patientRepository.save(patient);
        return mapper.toResponseDTO(saved);
    }

    // ✅ Buscar por id
    public Optional<PatientResponseDTO> buscarPorId(Long id) {
        return patientRepository.findById(id)
                .map(mapper::toResponseDTO);
    }

    // ✅ Buscar por DNI del dueño
    public Optional<PatientResponseDTO> buscarPorIdentificacion(String ownerDNI) {
        return patientRepository.findByownerDNI(ownerDNI)
                .map(mapper::toResponseDTO);
    }

    // ✅ Actualizar un paciente existente
    public Optional<PatientResponseDTO> actualizarPatient(Long id, PatientRequestDTO requestDTO) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(requestDTO.getName());
            patient.setBreed(requestDTO.getBreed());
            patient.setGender(requestDTO.getGender());
            patient.setAge(requestDTO.getAge());
            patient.setOwnerDNI(requestDTO.getOwnerDNI());
            patient.setOwnerName(requestDTO.getOwnerName());
            patient.setPhone(requestDTO.getPhone());

            Patient updated = patientRepository.save(patient);
            return mapper.toResponseDTO(updated);
        });
    }

    // ✅ Eliminar un paciente
    public void eliminarPatient(Long id) {
        patientRepository.deleteById(id);
    }
}
