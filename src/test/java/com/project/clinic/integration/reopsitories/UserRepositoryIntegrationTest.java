package com.project.clinic.integration.reopsitories;

import com.project.clinic.models.User;
import com.project.clinic.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test Find the user by username")
    public void testFindByUsername() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password"); // Set other required fields
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Test find by username not found")
    public void testFindByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonExistingUser");

        // Then
        assertFalse(foundUser.isPresent());
    }
}
