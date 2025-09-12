// package org.digital_academy.clinicaveterinaria;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// import org.digital_academy.clinicaveterinaria.paciente.model.Paciente;

// @Entity
// public class Cita {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private LocalDateTime fechaHora;

//     private String motivo;

//     @ManyToOne
//     @JoinColumn(name = "paciente_id")
//     private Paciente paciente;

//     // Getters y Setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public LocalDateTime getFechaHora() { return fechaHora; }
//     public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

//     public String getMotivo() { return motivo; }
//     public void setMotivo(String motivo) { this.motivo = motivo; }

//     public Paciente getPaciente() { return paciente; }
//     public void setPaciente(Paciente paciente) { this.paciente = paciente; }
// }
