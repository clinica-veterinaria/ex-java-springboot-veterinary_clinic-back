package org.digital_academy.treatment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentController(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    // Registrar un tratamiento
    @PostMapping
    public ResponseEntity<Treatment> createTreatment(@RequestBody Treatment treatment) {
        Treatment saved = treatmentRepository.save(treatment);
        return ResponseEntity.ok(saved);
    }

    // Ver historial de tratamientos de un paciente
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Treatment>> getTreatmentsByPatient(@PathVariable Long patientId) {
        List<Treatment> list = treatmentRepository.findByPatientId(patientId);
        return ResponseEntity.ok(list);
    }

    // Ver todos los tratamientos
    @GetMapping
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        return ResponseEntity.ok(treatmentRepository.findAll());
    }
}
