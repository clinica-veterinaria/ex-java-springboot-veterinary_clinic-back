package org.digital_academy.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.digital_academy.user.dto.UserRequestDTO;
import org.digital_academy.user.dto.UserResponseDTO;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios como DTO
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID como DTO
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserResponseDTO::fromEntity);
    }

    // Obtener usuario por email
    public Optional<UserResponseDTO> getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDTO::fromEntity);
    }

    // Crear nuevo usuario
    public UserResponseDTO createUser(UserRequestDTO requestDTO, MultipartFile photoFile) throws IOException {
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        UserEntity user = new UserEntity();
        user.setUsername(requestDTO.getEmail()); // username igual al email
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword())); // encriptar password

        // Asignar rol por defecto
        // user.setRoles(Set.of("USER"));

        user.setName(requestDTO.getName());
        user.setDni(requestDTO.getDni());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());

        if (photoFile != null && !photoFile.isEmpty()) {
            user.setPhoto(photoFile.getBytes());
        }

        UserEntity saved = userRepository.save(user);
        return UserResponseDTO.fromEntity(saved);
    }

    // Actualizar usuario existente
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO requestDTO, MultipartFile photoFile) throws IOException {
        return userRepository.findById(id).map(user -> {
            user.setName(requestDTO.getName());
            user.setDni(requestDTO.getDni());
            user.setEmail(requestDTO.getEmail());
            user.setPhone(requestDTO.getPhone());

            // Actualizar password si viene en DTO
            if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            }

            try {
                if (photoFile != null && !photoFile.isEmpty()) {
                    user.setPhoto(photoFile.getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error procesando la foto del usuario", e);
            }

            return UserResponseDTO.fromEntity(userRepository.save(user));
        });
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
