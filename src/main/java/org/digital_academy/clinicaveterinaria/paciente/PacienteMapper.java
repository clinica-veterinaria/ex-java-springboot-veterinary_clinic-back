package org.digital_academy.clinicaveterinaria.paciente;

// Update the import to the correct package where PacienteDTO exists
import org.digital_academy.clinicaveterinaria.paciente.PacienteDTO;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null) return null;

        return PacienteDTO.builder()
                .id(paciente.getId())
                .nombre(paciente.getNombre())
                .edad(paciente.getEdad())
                .raza(paciente.getRaza())
                .genero(paciente.getGenero())
                .numeroIdentificacion(paciente.getNumeroIdentificacion())
                .nombreTutor(paciente.getNombreTutor())
                .apellidosTutor(paciente.getApellidosTutor())
                .telefonoTutor(paciente.getTelefonoTutor())
                .build();
    }

    public Paciente toEntity(PacienteDTO dto) {
        if (dto == null) return null;

        return Paciente.builder()
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
