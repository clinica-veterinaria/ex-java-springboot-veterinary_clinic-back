package org.digital_academy.patient;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PatientServiceImpl implements InterfacePatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }
    @Override
    public List<PatientResponseDTO> listarPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public PatientResponseDTO guardarPatient(PatientRequestDTO requestDTO) {
        Patient paciente = patientMapper.toEntity(requestDTO);
        Patient saved = patientRepository.save(paciente);
        return patientMapper.toResponseDTO(saved);
    }
    @Override
    public Optional<PatientResponseDTO> buscarPorId(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toResponseDTO);
    }
    @Override
    public Optional<PatientResponseDTO> actualizarPatient(Long id, PatientRequestDTO requestDTO) {
        return patientRepository.findById(id)
                .map(existing -> {
                    // Actualizamos datos usando el mapper
                    patientMapper.updateEntityFromDto(requestDTO, existing);
                    Patient updated = patientRepository.save(existing);
                    return patientMapper.toResponseDTO(updated);
                });
    }
    @Override
    public void eliminarPatient(Long id) {
        patientRepository.deleteById(id);
    }
    @Override
    public Optional<PatientResponseDTO> buscarPorPetIdentification(String petIdentification) {
        return patientRepository.findByPetIdentification(petIdentification)
                .map(patientMapper::toResponseDTO);
    }
    @Override
    public Optional<PatientResponseDTO> buscarPorTutorDni(String tutorDni) {
        return patientRepository.findByTutorDni(tutorDni)
                .map(patientMapper::toResponseDTO);
    }
    @Override
    public Optional<PatientResponseDTO> buscarPorTutorEmail(String tutorEmail) {
        return patientRepository.findByTutorEmail(tutorEmail)
                .map(patientMapper::toResponseDTO);
    }
    @Override
    public Optional<PatientResponseDTO> buscarPorTutorPhone(String tutorPhone) {
        return patientRepository.findByTutorPhone(tutorPhone)
                .map(patientMapper::toResponseDTO);
    }
}






