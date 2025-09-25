package org.digital_academy.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // given
        UserEntity user1 = UserEntity.builder()
                .id(1L).username("alice").password("secret").roles(Set.of("USER"))
                .build();
        UserEntity user2 = UserEntity.builder()
                .id(2L).username("bob").password("pass").roles(Set.of("ADMIN"))
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // when
        List<UserEntity> users = userService.getAllUsers();

        // then
        assertEquals(2, users.size());
        assertEquals("alice", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_UserExists_ReturnsOptionalUser() {
        // given
        UserEntity user = UserEntity.builder()
                .id(1L).username("alice").password("secret").roles(Set.of("USER"))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        Optional<UserEntity> result = userService.getUserById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_UserDoesNotExist_ReturnsEmptyOptional() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getUserById(99L);

        assertTrue(result.isEmpty());
        verify(userRepository).findById(99L);
    }

    @Test
    void createUser_SavesAndReturnsUser() {
        // given
        UserEntity user = UserEntity.builder()
                .username("alice").password("secret").roles(Set.of("USER"))
                .build();

        when(userRepository.save(user)).thenReturn(user);

        // when
        UserEntity created = userService.createUser(user);

        // then
        assertEquals("alice", created.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_DeletesById() {
        // when
        userService.deleteUser(1L);

        // then
        verify(userRepository).deleteById(1L);
    }
}
