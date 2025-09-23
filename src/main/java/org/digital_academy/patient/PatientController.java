package org.digital_academy.patient;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.digital_academy.implementation.IPatientService;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final IPatientService<PatientResponseDTO, PatientRequestDTO> patientService;

    public PatientController(IPatientService<PatientResponseDTO, PatientRequestDTO> patientService) {
        this.patientService = patientService;
    }

    // ✅ GET -> Listar todos los pacientes (solo ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<PatientResponseDTO> listarPatients() {
        return patientService.listarPatients();
    }

    // ✅ POST -> Crear un nuevo paciente (solo ADMIN y USER)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public PatientResponseDTO crearPatient(@RequestBody PatientRequestDTO requestDTO) {
        return patientService.guardarPatient(requestDTO);
    }

    // ✅ GET -> Obtener un paciente por id (solo ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public PatientResponseDTO obtenerPatient(@PathVariable Long id) {
        return patientService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // ✅ PUT -> Actualizar un paciente (solo ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public PatientResponseDTO actualizarPatient(@PathVariable Long id, @RequestBody PatientRequestDTO requestDTO) {
        return patientService.actualizarPatient(id, requestDTO)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // ✅ DELETE -> Eliminar un paciente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminarPatient(@PathVariable Long id) {
        patientService.eliminarPatient(id);
    }
}
