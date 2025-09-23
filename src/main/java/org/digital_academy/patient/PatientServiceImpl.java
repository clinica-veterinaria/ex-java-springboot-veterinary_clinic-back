package org.digital_academy.patient;

import org.digital_academy.implementation.IPatientService;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements IPatientService <PatientResponseDTO, PatientRequestDTO> {

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
    public Optional<PatientResponseDTO> buscarPorOwnerDNI(String dniPropietario) {
        return patientRepository.findByOwnerDNI(dniPropietario)
                .map(patientMapper::toResponseDTO);
    }

    @Override
    public Optional<PatientResponseDTO> buscarPorCampo(String campo) {
        
        return patientRepository.findByName(campo)
                .map(patientMapper::toResponseDTO);
    }

    @Override
    public Optional<PatientResponseDTO> buscarPorTelefono(String telefono) {
        return patientRepository.findByPhone(telefono)
                .map(patientMapper::toResponseDTO);
    }

}