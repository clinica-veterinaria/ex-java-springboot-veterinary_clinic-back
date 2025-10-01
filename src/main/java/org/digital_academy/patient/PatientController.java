package org.digital_academy.patient;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Listar todos los pacientes
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<PatientResponseDTO> listarPatients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String sortBy) {

        if ((search == null || search.isEmpty()) &&
                (breed == null || breed.isEmpty()) &&
                (gender == null || gender.isEmpty()) &&
                (sortBy == null || sortBy.isEmpty())) {
            return patientService.getAllPatients();
        }

        return patientService.searchPatients(search, breed, gender, sortBy);
    }

    // Crear nuevo paciente
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PatientResponseDTO> createPatient(
            @RequestPart("patient") PatientRequestDTO requestDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        // DEBUG: Verificar autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== DEBUG PATIENT CONTROLLER ===");
        System.out.println("🔍 Usuario autenticado: " + auth.getName());
        System.out.println("🔍 Roles/autoridades: " + auth.getAuthorities());
        System.out.println("🔍 Está autenticado: " + auth.isAuthenticated());
        System.out.println("🔍 Principal: " + auth.getPrincipal());
        System.out.println("=== FIN DEBUG ===");

        try {
            PatientResponseDTO newPatient = patientService.createPatient(requestDTO, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener paciente por ID
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public PatientResponseDTO obtenerPatient(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // Actualizar paciente
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public PatientResponseDTO actualizarPatient(@PathVariable Long id, @RequestBody PatientRequestDTO requestDTO) {
        return patientService.updatePatient(id, requestDTO)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // Eliminar paciente
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminarPatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    // Buscar por número de identificación del paciente
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/petIdentification/{petIdentification}")
    public PatientResponseDTO getByPetIdentification(@PathVariable String petIdentification) {
        return patientService.getByPetIdentification(petIdentification)
                .orElseThrow(
                        () -> new RuntimeException("Patient no encontrado con identificación " + petIdentification));
    }

    // Buscar por DNI del tutor
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/tutorDni/{tutorDni}")
    public PatientResponseDTO getByTutorDni(@PathVariable String tutorDni) {
        return patientService.getByTutorDni(tutorDni)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con DNI del tutor " + tutorDni));
    }

    // Buscar por teléfono del tutor
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/tutorPhone/{tutorPhone}")
    public PatientResponseDTO getByTutorPhone(@PathVariable String tutorPhone) {
        return patientService.getByTutorPhone(tutorPhone)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con teléfono del tutor " + tutorPhone));
    }

    // Buscar por email del tutor
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/tutorEmail/{tutorEmail}")
    public PatientResponseDTO getByTutorEmail(@PathVariable String tutorEmail) {
        return patientService.getByTutorEmail(tutorEmail)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con email del tutor " + tutorEmail));
    }

}