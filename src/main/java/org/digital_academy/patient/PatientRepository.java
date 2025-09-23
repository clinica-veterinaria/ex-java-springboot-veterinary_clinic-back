package org.digital_academy.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByOwnerDNI(String ownerDNI);
    Optional<Patient> findByPhone(String phone);
    Optional<Patient> findByName(String name);
}
