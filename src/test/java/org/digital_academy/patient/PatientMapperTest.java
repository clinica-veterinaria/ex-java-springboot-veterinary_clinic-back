package org.digital_academy.patient;

import static org.junit.jupiter.api.Assertions.*;

import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatientMapperTest {

    private PatientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PatientMapper();
    }

    @Test
    void toResponseDTO_ShouldMapAllFields() {
        Patient patient = Patient.builder()
                .id(1L)
                .name("Fido")
                .age(5)
                .breed("Labrador")
                .gender("Male")
                .petIdentification("123ABC")
                .tutorName("Alice")
                .tutorDni("12345678A")
                .tutorPhone("555-1234")
                .tutorEmail("alice@example.com")
                .build();

        PatientResponseDTO dto = mapper.toResponseDTO(patient);

        assertNotNull(dto);
        assertEquals(patient.getId(), dto.getId());
        assertEquals(patient.getName(), dto.getName());
        assertEquals(patient.getAge(), dto.getAge());
        assertEquals(patient.getBreed(), dto.getBreed());
        assertEquals(patient.getGender(), dto.getGender());
        assertEquals(patient.getPetIdentification(), dto.getPetIdentification());
        assertEquals(patient.getTutorName(), dto.getTutorName());
        assertEquals(patient.getTutorDni(), dto.getTutorDni());
        assertEquals(patient.getTutorPhone(), dto.getTutorPhone());
        assertEquals(patient.getTutorEmail(), dto.getTutorEmail());
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        PatientRequestDTO request = PatientRequestDTO.builder()
                .name("Fido")
                .age(5)
                .breed("Labrador")
                .gender("Male")
                .petIdentification("123ABC")
                .tutorName("Alice")
                .tutorDni("12345678A")
                .tutorPhone("555-1234")
                .tutorEmail("alice@example.com")
                .build();

        Patient patient = mapper.toEntity(request);

        assertNotNull(patient);
        assertEquals(request.getName(), patient.getName());
        assertEquals(request.getAge(), patient.getAge());
        assertEquals(request.getBreed(), patient.getBreed());
        assertEquals(request.getGender(), patient.getGender());
        assertEquals(request.getPetIdentification(), patient.getPetIdentification());
        assertEquals(request.getTutorName(), patient.getTutorName());
        assertEquals(request.getTutorDni(), patient.getTutorDni());
        assertEquals(request.getTutorPhone(), patient.getTutorPhone());
        assertEquals(request.getTutorEmail(), patient.getTutorEmail());
    }

    @Test
    void updateEntityFromDto_ShouldUpdateAllFields() {
        PatientRequestDTO request = PatientRequestDTO.builder()
                .name("Rex")
                .age(6)
                .breed("Beagle")
                .gender("Male")
                .petIdentification("456DEF")
                .tutorName("Bob")
                .tutorDni("87654321B")
                .tutorPhone("555-5678")
                .tutorEmail("bob@example.com")
                .build();

        Patient patient = Patient.builder()
                .name("OldName")
                .age(3)
                .breed("OldBreed")
                .gender("Female")
                .petIdentification("000")
                .tutorName("OldTutor")
                .tutorDni("00000000X")
                .tutorPhone("000-0000")
                .tutorEmail("old@example.com")
                .build();

        mapper.updateEntityFromDto(request, patient);

        assertEquals(request.getName(), patient.getName());
        assertEquals(request.getAge(), patient.getAge());
        assertEquals(request.getBreed(), patient.getBreed());
        assertEquals(request.getGender(), patient.getGender());
        assertEquals(request.getPetIdentification(), patient.getPetIdentification());
        assertEquals(request.getTutorName(), patient.getTutorName());
        assertEquals(request.getTutorDni(), patient.getTutorDni());
        assertEquals(request.getTutorPhone(), patient.getTutorPhone());
        assertEquals(request.getTutorEmail(), patient.getTutorEmail());
    }

    @Test
    void toResponseDTO_ShouldReturnNull_WhenInputIsNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void updateEntityFromDto_ShouldDoNothing_WhenInputIsNull() {
        Patient patient = Patient.builder().build();
        mapper.updateEntityFromDto(null, patient);
        mapper.updateEntityFromDto(new PatientRequestDTO(), null);
    }
}
