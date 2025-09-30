package org.digital_academy.user;

import org.digital_academy.user.UserEntity;
import org.digital_academy.user.UserRepository;
import org.digital_academy.user.UserService;
import org.digital_academy.user.dto.UserRequestDTO;
import org.digital_academy.user.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setDni("12345678X");
        userEntity.setPhone("555-1234");
        userEntity.setPassword("encodedPassword");

        requestDTO = UserRequestDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .dni("12345678X")
                .phone("555-1234")
                .password("password123")
                .build();
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        Optional<UserResponseDTO> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void testGetByEmail_Found() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));

        Optional<UserResponseDTO> result = userService.getByEmail("john@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getByEmail("notfound@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void testCreateUser_WithPhoto() throws IOException {
        MockMultipartFile photo = new MockMultipartFile(
                "photo",
                "photo.jpg",
                "image/jpeg",
                "fake-image".getBytes());

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponseDTO result = userService.createUser(requestDTO, photo);

        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void testCreateUser_WithoutPassword_ThrowsException() throws IOException {
        requestDTO.setPassword(null);

        try {
            userService.createUser(requestDTO, null);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("La contraseña no puede estar vacía");
        }

        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_WithPhoto() throws IOException {
        MockMultipartFile photo = new MockMultipartFile(
                "photo",
                "photo.jpg",
                "image/jpeg",
                "fake-image".getBytes());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(any())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        Optional<UserResponseDTO> result = userService.updateUser(1L, requestDTO, photo);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void testUpdateUser_NotFound() throws IOException {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.updateUser(99L, requestDTO, null);

        assertThat(result).isEmpty();
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
