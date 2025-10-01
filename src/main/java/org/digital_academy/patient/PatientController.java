package org.digital_academy.patient;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
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
    @GetMapping
    public List<PatientResponseDTO> listarPatients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String sortBy) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("========== GET /patients ==========");
        System.out.println("========== GET /patients ==========");
        System.out.println("Autenticado: " + auth.isAuthenticated());
        System.out.println("Roles: " + auth.getAuthorities());
        System.out.println("===================================");

        if ((search == null || search.isEmpty()) &&
                (breed == null || breed.isEmpty()) &&
                (gender == null || gender.isEmpty()) &&
                (sortBy == null || sortBy.isEmpty())) {
            return patientService.getAllPatients();
        }

        return patientService.searchPatients(search, breed, gender, sortBy);
    }

    // Crear nuevo paciente
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PatientResponseDTO> createPatient(
            @RequestPart("patient") PatientRequestDTO requestDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        try {
            PatientResponseDTO newPatient = patientService.createPatient(requestDTO, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener paciente por ID
    @GetMapping("/{id}")
    public PatientResponseDTO obtenerPatient(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // Actualizar paciente
    @PutMapping("/{id}")
    public PatientResponseDTO actualizarPatient(@PathVariable Long id, @RequestBody PatientRequestDTO requestDTO) {
        return patientService.updatePatient(id, requestDTO)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con id " + id));
    }

    // Eliminar paciente
    @DeleteMapping("/{id}")
    public void eliminarPatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    // Buscar por número de identificación del paciente
    @GetMapping("/petIdentification/{petIdentification}")
    public PatientResponseDTO getByPetIdentification(@PathVariable String petIdentification) {
        return patientService.getByPetIdentification(petIdentification)
                .orElseThrow(
                        () -> new RuntimeException("Patient no encontrado con identificación " + petIdentification));
    }

    // Buscar por DNI del tutor
    @GetMapping("/tutorDni/{tutorDni}")
    public PatientResponseDTO getByTutorDni(@PathVariable String tutorDni) {
        return patientService.getByTutorDni(tutorDni)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con DNI del tutor " + tutorDni));
    }

    // Buscar por teléfono del tutor
    @GetMapping("/tutorPhone/{tutorPhone}")
    public PatientResponseDTO getByTutorPhone(@PathVariable String tutorPhone) {
        return patientService.getByTutorPhone(tutorPhone)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con teléfono del tutor " + tutorPhone));
    }

    // Buscar por email del tutor
    @GetMapping("/tutorEmail/{tutorEmail}")
    public PatientResponseDTO getByTutorEmail(@PathVariable String tutorEmail) {
        return patientService.getByTutorEmail(tutorEmail)
                .orElseThrow(() -> new RuntimeException("Patient no encontrado con email del tutor " + tutorEmail));
    }

}