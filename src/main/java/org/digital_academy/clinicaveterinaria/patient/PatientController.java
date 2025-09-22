package org.digital_academy.clinicaveterinaria.patient;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.digital_academy.clinicaveterinaria.implementation.IPatientService;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final IPatientService<PatientResponseDTO, PatientRequestDTO> patientService;

    public PatientController(IPatientService<PatientResponseDTO, PatientRequestDTO> patientService) {
        this.patientService = patientService;
    }

    // ✅ GET -> Listar todos los pacientes
    @GetMapping
    public List<PatientResponseDTO> listarPatients() {
        return patientService.listarPatients();
    }

    // ✅ POST -> Crear un nuevo paciente
    @PostMapping
    public PatientResponseDTO crearPatient(@RequestBody PatientRequestDTO requestDTO) {
        return patientService.guardarPatient(requestDTO);
    }

    // ✅ GET -> Obtener un paciente por id
    @GetMapping("/{id}")
    public PatientResponseDTO obtenerPatient(@PathVariable Long id) {
        return patientService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // ✅ PUT -> Actualizar un paciente existente
    @PutMapping("/{id}")
    public PatientResponseDTO actualizarPatient(@PathVariable Long id, @RequestBody PatientRequestDTO requestDTO) {
        return patientService.actualizarPatient(id, requestDTO)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // ✅ DELETE -> Eliminar un paciente
    @DeleteMapping("/{id}")
    public void eliminarPatient(@PathVariable Long id) {
        patientService.eliminarPatient(id);
    }
}
