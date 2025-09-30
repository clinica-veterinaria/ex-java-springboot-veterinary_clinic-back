package org.digital_academy.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;

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
        requestDTO.setName("Bobby");

        responseDTO = new PatientResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setPetIdentification("PET123");
        responseDTO.setName("Bobby");
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
        assertThat(result.get().getName()).isEqualTo("Bobby");
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PatientResponseDTO> result = patientService.getPatientById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void testCreatePatientWithoutImage() throws IOException {
        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientService.createPatient(requestDTO, null);

        assertThat(result.getName()).isEqualTo("Bobby");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void testCreatePatientWithImage() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "photo.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());

        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientService.createPatient(requestDTO, imageFile);

        assertThat(result.getName()).isEqualTo("Bobby");
        verify(patientRepository, times(1)).save(any(Patient.class));
        assertThat(patient.getImage()).isNotNull();
    }

    @Test
    void testCreatePatient_IOException() throws IOException {
        MockMultipartFile imageFile = mock(MockMultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getBytes()).thenThrow(new IOException());

        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);

        try {
            patientService.createPatient(requestDTO, imageFile);
        } catch (IOException e) {
            assertThat(e).isInstanceOf(IOException.class);
        }
    }

    @Test
    void testUpdatePatient_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientMapper).updateEntityFromDto(requestDTO, patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.updatePatient(1L, requestDTO);

        assertThat(result).isPresent();
        verify(patientMapper, times(1)).updateEntityFromDto(requestDTO, patient);
        verify(patientRepository, times(1)).save(patient);
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
    void testGetByPetIdentification_Found() {
        when(patientRepository.findByPetIdentification("PET123")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByPetIdentification("PET123");

        assertThat(result).isPresent();
    }

    @Test
    void testGetByPetIdentification_NotFound() {
        when(patientRepository.findByPetIdentification("PET999")).thenReturn(Optional.empty());

        Optional<PatientResponseDTO> result = patientService.getByPetIdentification("PET999");

        assertThat(result).isEmpty();
    }

    @Test
    void testGetByTutorDni_Found() {
        when(patientRepository.findByTutorDni("12345678X")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorDni("12345678X");

        assertThat(result).isPresent();
    }

    @Test
    void testGetByTutorPhone_Found() {
        when(patientRepository.findByTutorPhone("555-1234")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorPhone("555-1234");

        assertThat(result).isPresent();
    }

    @Test
    void testGetByTutorEmail_Found() {
        when(patientRepository.findByTutorEmail("john@example.com")).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        Optional<PatientResponseDTO> result = patientService.getByTutorEmail("john@example.com");

        assertThat(result).isPresent();
    }

    @Test
    void testSearchPatients() {
        when(patientRepository.searchWithFilters("Bobby", "Beagle", "Male", "name"))
                .thenReturn(Arrays.asList(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(responseDTO);

        List<PatientResponseDTO> result = patientService.searchPatients("Bobby", "Beagle", "Male", "name");

        assertThat(result).hasSize(1);
        verify(patientRepository, times(1)).searchWithFilters("Bobby", "Beagle", "Male", "name");
    }
}
