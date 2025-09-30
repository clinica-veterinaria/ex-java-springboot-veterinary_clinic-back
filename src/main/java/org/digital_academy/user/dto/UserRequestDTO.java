package org.digital_academy.user.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String dni;
    private String email;
    private String phone;
    private String password;  
}
