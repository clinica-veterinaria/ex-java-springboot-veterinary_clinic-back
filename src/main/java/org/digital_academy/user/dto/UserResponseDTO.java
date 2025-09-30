package org.digital_academy.user.dto;

import lombok.Data;
import org.digital_academy.user.UserEntity;
import java.util.Base64;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String dni;
    private String email;
    private String phone;
    private String photo; // Base64 para React

    public static UserResponseDTO fromEntity(UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDni(user.getDni());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        if (user.getPhoto() != null) {
            dto.setPhoto(Base64.getEncoder().encodeToString(user.getPhoto()));
        }
        return dto;
    }
}
