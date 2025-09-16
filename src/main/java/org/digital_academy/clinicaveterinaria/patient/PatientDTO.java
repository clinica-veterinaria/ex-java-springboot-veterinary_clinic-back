package org.digital_academy.clinicaveterinaria.patient;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {
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
