package org.digital_academy.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.digital_academy.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private PasswordEncoder passwordEncoder;

        @MockBean
        private AuthenticationManager authenticationManager;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void testRegister_Success() throws Exception {
                RegisterRequest request = new RegisterRequest();
                request.setEmail("test@example.com");
                request.setPassword("secret");
                request.setName("Alice");

                // JSON як String
                String userDataJson = objectMapper.writeValueAsString(request);
                MockMultipartFile userData = new MockMultipartFile("userData", "", "application/json",
                                userDataJson.getBytes());
                MockMultipartFile photo = new MockMultipartFile("photo", "photo.png", "image/png",
                                "fakeimage".getBytes());

                // Моки
                Mockito.when(userRepository.findByUsername(eq("test@example.com"))).thenReturn(Optional.empty());
                Mockito.when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
                Mockito.when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

                mockMvc.perform(multipart("/auth/register")
                                .file(userData)
                                .file(photo)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isOk())
                                .andExpect(content().string("✅ Usuario registrado con éxito"));
        }

        @Test
        void testLogin_Success() throws Exception {
                LoginRequest request = new LoginRequest();
                request.setEmail("test@example.com");
                request.setPassword("secret");

                Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "secret"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("✅ Login exitoso"));
        }

        @Test
        void testLogin_Failure() throws Exception {
                LoginRequest request = new LoginRequest();
                request.setEmail("wrong@example.com");
                request.setPassword("bad");

                Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Invalid"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(content().string("❌ Usuario o contraseña incorrectos"));
        }
}
