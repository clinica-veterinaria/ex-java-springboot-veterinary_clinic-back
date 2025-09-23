package org.digital_academy.patient;
import java.util.List;
import java.util.Optional;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
public interface InterfacePatientService {
    // Listar todos los pacientes
    List<PatientResponseDTO> listarPatients();
    // Guardar nuevo paciente
    PatientResponseDTO guardarPatient(PatientRequestDTO requestDTO);
    // Buscar por ID interno
    Optional<PatientResponseDTO> buscarPorId(Long id);
    // Actualizar paciente
    Optional<PatientResponseDTO> actualizarPatient(Long id, PatientRequestDTO requestDTO);
    // Eliminar paciente
    void eliminarPatient(Long id);
    // Búsqueda por número de identificación del paciente
    Optional<PatientResponseDTO> buscarPorPetIdentification(String petIdentification);
    // Búsqueda por DNI del tutor
    Optional<PatientResponseDTO> buscarPorTutorDni(String tutorDni);
    // Búsqueda por email del tutor
    Optional<PatientResponseDTO> buscarPorTutorEmail(String tutorEmail);
    // Búsqueda por teléfono del tutor
    Optional<PatientResponseDTO> buscarPorTutorPhone(String tutorPhone);
}






