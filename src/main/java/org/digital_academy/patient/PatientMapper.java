package org.digital_academy.patient;

import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
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
                .ownerDNI(patient.getOwnerDNI())
                .ownerName(patient.getOwnerName())
                .phone(patient.getPhone())
                .build();
    }

    
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
