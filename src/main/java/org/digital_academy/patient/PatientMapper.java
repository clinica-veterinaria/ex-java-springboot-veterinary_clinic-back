package org.digital_academy.patient;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.springframework.stereotype.Component;
@Component
public class PatientMapper {
    public PatientResponseDTO toResponseDTO(Patient patient) {
        if (patient == null) return null;
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .breed(patient.getBreed())
                .gender(patient.getGender())
                .petIdentification(patient.getPetIdentification())
                .tutorName(patient.getTutorName())
                .tutorDni(patient.getTutorDni())
                .tutorPhone(patient.getTutorPhone())
                .tutorEmail(patient.getTutorEmail())
                .build();
    }
    public Patient toEntity(PatientRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return Patient.builder()
                .name(requestDTO.getName())
                .age(requestDTO.getAge())
                .breed(requestDTO.getBreed())
                .gender(requestDTO.getGender())
                .petIdentification(requestDTO.getPetIdentification())
                .tutorName(requestDTO.getTutorName())
                .tutorDni(requestDTO.getTutorDni())
                .tutorPhone(requestDTO.getTutorPhone())
                .tutorEmail(requestDTO.getTutorEmail())
                .build();
    }
    // ← Método nuevo para actualizar entidad existente
    public void updateEntityFromDto(PatientRequestDTO dto, Patient entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setAge(dto.getAge());
        entity.setBreed(dto.getBreed());
        entity.setGender(dto.getGender());
        entity.setPetIdentification(dto.getPetIdentification());
        entity.setTutorName(dto.getTutorName());
        entity.setTutorDni(dto.getTutorDni());
        entity.setTutorPhone(dto.getTutorPhone());
        entity.setTutorEmail(dto.getTutorEmail());
    }
}









