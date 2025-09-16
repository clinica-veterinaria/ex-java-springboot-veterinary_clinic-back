package org.digital_academy.clinicaveterinaria.paciente;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // GET -> Listar todos los pacientes
    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // POST -> Crear un nuevo paciente
    @PostMapping
    public Paciente crearPaciente(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // GET -> Obtener un paciente por id
    @GetMapping("/{id}")
    public Paciente obtenerPaciente(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id " + id));
    }

    // PUT -> Actualizar un paciente existente
    @PutMapping("/{id}")
    public Paciente actualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteActualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNombre(pacienteActualizado.getNombre());
            paciente.setRaza(pacienteActualizado.getRaza());
            paciente.setGenero(pacienteActualizado.getGenero());
            paciente.setEdad(pacienteActualizado.getEdad());
            paciente.setNumeroIdentificacion(pacienteActualizado.getNumeroIdentificacion());
            paciente.setNombreTutor(pacienteActualizado.getNombreTutor());
            paciente.setApellidosTutor(pacienteActualizado.getApellidosTutor());
            paciente.setTelefonoTutor(pacienteActualizado.getTelefonoTutor());
            return pacienteRepository.save(paciente);
        }).orElseThrow(() -> new RuntimeException("Paciente no encontrado con id " + id));
    }

    // DELETE -> Eliminar un paciente
    @DeleteMapping("/{id}")
    public void eliminarPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
    }
}
