package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

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
                    existing.setName(requestDTO.getName());
                    existing.setAge(requestDTO.getAge());
                    existing.setBreed(requestDTO.getBreed());
                    existing.setGender(requestDTO.getGender());
                    existing.setOwnerDNI(requestDTO.getOwnerDNI());
                    existing.setOwnerName(requestDTO.getOwnerName());
                    existing.setPhone(requestDTO.getPhone());
                    Patient updated = patientRepository.save(existing);
                    return patientMapper.toResponseDTO(updated);
                });
    }

    @Override
    public void eliminarPatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Optional<PatientResponseDTO> buscarPorPhone(String phone) {
        return patientRepository.findByPhone(phone)
                .map(patientMapper::toResponseDTO);
    }

    @Override
    public Optional<PatientResponseDTO> buscarPorOwnerDNI(String ownerDNI) {
        return patientRepository.findByOwnerDNI(ownerDNI)
                .map(patientMapper::toResponseDTO);
    }
}
