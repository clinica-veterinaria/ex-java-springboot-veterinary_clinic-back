package org.digital_academy.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username; 
    private String password;
    private String role; 

    private String name;
    private String dni;
    private String email;
    private String phone; 
}
