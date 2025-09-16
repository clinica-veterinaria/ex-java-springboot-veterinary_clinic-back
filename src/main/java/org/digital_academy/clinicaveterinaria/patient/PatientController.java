package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // GET -> Listar todos los patients
    @GetMapping
    public List<Patient> listarpatients() {
        return patientRepository.findAll();
    }

    // POST -> Crear un nuevo patient
    @PostMapping
    public Patient crearpatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    // GET -> Obtener un patient por id
    @GetMapping("/{id}")
    public Patient obtenerpatient(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("patient no encontrado con id " + id));
    }

    // PUT -> Actualizar un patient existente
    @PutMapping("/{id}")
    public Patient actualizarpatient(@PathVariable Long id, @RequestBody Patient patientActualizado) {
        return patientRepository.findById(id).map(patient -> {
            patient.setNombre(patientActualizado.getNombre());
            patient.setRaza(patientActualizado.getRaza());
            patient.setGenero(patientActualizado.getGenero());
            patient.setEdad(patientActualizado.getEdad());
            patient.setNumeroIdentificacion(patientActualizado.getNumeroIdentificacion());
            patient.setNombreTutor(patientActualizado.getNombreTutor());
            patient.setApellidosTutor(patientActualizado.getApellidosTutor());
            patient.setTelefonoTutor(patientActualizado.getTelefonoTutor());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("patient no encontrado con id " + id));
    }

    // DELETE -> Eliminar un patient
    @DeleteMapping("/{id}")
    public void eliminarpatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
    }
}
