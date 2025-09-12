package org.digital_academy.clinicaveterinaria.paciente.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDTO {
    private Long id;
    private String nombre;
    private int edad;
    private String raza;
    private String genero;
    private String numeroIdentificacion;

    private String nombreTutor;
    private String apellidosTutor;
    private String telefonoTutor;
}
