package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    // 🔹 Entity -> ResponseDTO
    public PatientResponseDTO toResponseDTO(Patient patient) {
        if (patient == null) return null;

        return PatientResponseDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .breed(patient.getBreed())
                .gender(patient.getGender())
                .ownerDNI(patient.getOwnerDNI())
                .ownerName(patient.getOwnerName())
                .phone(patient.getPhone())
                .build();
    }

    // 🔹 RequestDTO -> Entity
    public Patient toEntity(PatientRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        return Patient.builder()
                .name(requestDTO.getName())
                .age(requestDTO.getAge())
                .breed(requestDTO.getBreed())
                .gender(requestDTO.getGender())
                .ownerDNI(requestDTO.getOwnerDNI())
                .ownerName(requestDTO.getOwnerName())
                .phone(requestDTO.getPhone())
                .build();
    }

    
}
