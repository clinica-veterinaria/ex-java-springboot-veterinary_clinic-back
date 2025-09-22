package org.digital_academy.clinicaveterinaria.patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    // Listar todos los pacientes
    List<PatientResponseDTO> listarPatients();

    // Guardar un nuevo paciente
    PatientResponseDTO guardarPatient(PatientRequestDTO requestDTO);

    // Buscar paciente por id
    Optional<PatientResponseDTO> buscarPorId(Long id);

    // Actualizar paciente existente
    Optional<PatientResponseDTO> actualizarPatient(Long id, PatientRequestDTO requestDTO);

    // Eliminar paciente
    void eliminarPatient(Long id);

    // Opcional: buscar paciente por ownerDNI
    Optional<PatientResponseDTO> buscarPorOwnerDNI(String ownerDNI);

    // Opcional: buscar paciente por teléfono
    Optional<PatientResponseDTO> buscarPorPhone(String phone);
}
