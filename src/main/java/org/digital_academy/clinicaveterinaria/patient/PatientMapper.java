package org.digital_academy.clinicaveterinaria.patient;

import org.digital_academy.clinicaveterinaria.patient.PatientDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDTO toDTO(Patient patient) {
        if (patient == null) return null;

        return PatientDTO.builder()
                .id(patient.getId())
                .nombre(patient.getNombre())
                .edad(patient.getEdad())
                .raza(patient.getRaza())
                .genero(patient.getGenero())
                .numeroIdentificacion(patient.getNumeroIdentificacion())
                .nombreTutor(patient.getNombreTutor())
                .apellidosTutor(patient.getApellidosTutor())
                .telefonoTutor(patient.getTelefonoTutor())
                .build();
    }

    public Patient toEntity(PatientDTO dto) {
        if (dto == null) return null;

        return Patient.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .edad(dto.getEdad())
                .raza(dto.getRaza())
                .genero(dto.getGenero())
                .numeroIdentificacion(dto.getNumeroIdentificacion())
                .nombreTutor(dto.getNombreTutor())
                .apellidosTutor(dto.getApellidosTutor())
                .telefonoTutor(dto.getTelefonoTutor())
                .build();
    }
}
