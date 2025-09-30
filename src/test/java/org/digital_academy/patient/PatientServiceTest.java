package org.digital_academy.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.digital_academy.patient.dto.PatientRequestDTO;
import org.digital_academy.patient.dto.PatientResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientRequestDTO requestDTO;
    private PatientResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setPetIdentification("PET123");

        requestDTO = new PatientRequestDTO();

        responseDTO = new PatientResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setPetIdentification("PET123");
    }

    @Test
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        List<PatientResponseDTO> result = patientService.getAllPatients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPetIdentification()).isEqualTo("PET123");

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getPatientById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getPetIdentification()).isEqualTo("PET123");
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PatientResponseDTO> result = patientService.getPatientById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void testCreatePatient() {
        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientService.createPatient(requestDTO);

        assertThat(result.getPetIdentification()).isEqualTo("PET123");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void testUpdatePatient_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doAnswer(invocation -> {
            // симулюємо оновлення сутності
            return null;
        }).when(patientMapper).updateEntityFromDto(requestDTO, patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.updatePatient(1L, requestDTO);

        assertThat(result).isPresent();
        assertThat(result.get().getPetIdentification()).isEqualTo("PET123");
    }

    @Test
    void testUpdatePatient_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PatientResponseDTO> result = patientService.updatePatient(99L, requestDTO);

        assertThat(result).isEmpty();
    }

    @Test
    void testDeletePatient() {
        doNothing().when(patientRepository).deleteById(1L);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetByPetIdentification() {
        when(patientRepository.findByPetIdentification("PET123")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByPetIdentification("PET123");

        assertThat(result).isPresent();
        assertThat(result.get().getPetIdentification()).isEqualTo("PET123");
    }

    @Test
    void testGetByTutorDni() {
        when(patientRepository.findByTutorDni("12345678A")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorDni("12345678A");

        assertThat(result).isPresent();
    }

    @Test
    void testGetByTutorPhone() {
        when(patientRepository.findByTutorPhone("600123123")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorPhone("600123123");

        assertThat(result).isPresent();
    }

    @Test
    void testGetByTutorEmail() {
        when(patientRepository.findByTutorEmail("test@mail.com")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorEmail("test@mail.com");

        assertThat(result).isPresent();
    }
}
