package org.digital_academy.treatment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
	List<Treatment> findByPatientId(Long patientId);
}
