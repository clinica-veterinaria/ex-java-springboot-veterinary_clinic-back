package org.digital_academy.user;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import org.digital_academy.user.dto.UserRequestDTO;
import org.digital_academy.user.dto.UserResponseDTO;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Listar todos los usuarios
    @GetMapping
    public List<UserResponseDTO> listarUsers() {
        return userService.getAllUsers();
    }

    // Crear usuario desde JSON
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(requestDTO, null));
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public UserResponseDTO obtenerUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    // Actualizar usuario desde JSON
    @PutMapping("/{id}")
    public UserResponseDTO actualizarUser(@PathVariable Long id, @RequestBody UserRequestDTO requestDTO) throws IOException {
        return userService.updateUser(id, requestDTO, null)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Buscar usuario por email
    @GetMapping("/email/{email}")
    public UserResponseDTO getByEmail(@PathVariable String email) {
        return userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email " + email));
    }
}
