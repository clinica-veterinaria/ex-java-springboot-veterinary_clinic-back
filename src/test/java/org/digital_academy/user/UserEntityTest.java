package org.digital_academy.user;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class UserEntityTest {

    @Test
    void testUserEntityCreation() {
        UserEntity user = UserEntity.builder()
                .username("testuser")
                .password("password123")
                .roles(Set.of("ROLE_USER"))
                .build();

        assert user.getUsername().equals("testuser");
        assert user.getPassword().equals("password123");
        assert user.getRoles().contains("ROLE_USER");
    }
}
