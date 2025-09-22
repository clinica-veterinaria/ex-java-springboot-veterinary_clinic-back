package org.digital_academy.clinicaveterinaria.patient;

import java.util.List;
import java.util.Optional;

public interface InterfacePatientService {
    List<PatientResponseDTO> listarPatients();
    PatientResponseDTO guardarPatient(PatientRequestDTO requestDTO);
    Optional<PatientResponseDTO> buscarPorId(Long id);
    Optional<PatientResponseDTO> actualizarPatient(Long id, PatientRequestDTO requestDTO);
    void eliminarPatient(Long id);
}
