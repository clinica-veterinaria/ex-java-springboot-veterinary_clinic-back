package org.digital_academy.treatment;

import org.digital_academy.patient.Patient;
import org.digital_academy.patient.PatientRepository;
import org.digital_academy.treatment.dto.TreatmentRequestDTO;
import org.digital_academy.treatment.dto.TreatmentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;
    private final TreatmentMapper treatmentMapper;

    @Autowired
    public TreatmentController(TreatmentRepository treatmentRepository,
                               PatientRepository patientRepository,
                               TreatmentMapper treatmentMapper) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
        this.treatmentMapper = treatmentMapper;
    }




    // Crear un tratamiento
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TreatmentResponseDTO> createTreatment(@RequestBody TreatmentRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Treatment saved = treatmentRepository.save(treatmentMapper.toEntity(requestDTO, patient));
        return ResponseEntity.ok(treatmentMapper.toResponseDTO(saved));
    }

    // Historial de un paciente
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentResponseDTO>> getTreatmentsByPatient(@PathVariable Long patientId) {
        List<TreatmentResponseDTO> list = treatmentRepository.findByPatientId(patientId)
                                        .stream()
                                        .map(treatmentMapper::toResponseDTO)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Todos los tratamientos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TreatmentResponseDTO>> getAllTreatments() {
        List<TreatmentResponseDTO> list = treatmentRepository.findAll()
                                        .stream()
                                        .map(treatmentMapper::toResponseDTO)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
