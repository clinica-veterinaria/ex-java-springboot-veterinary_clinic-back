package org.digital_academy.clinicaveterinaria.paciente.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
