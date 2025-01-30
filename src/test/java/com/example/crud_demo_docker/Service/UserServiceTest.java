package com.example.crud_demo_docker.Service;

import com.example.crud_demo_docker.Controller.UserController;
import com.example.crud_demo_docker.Entity.User;
import com.example.crud_demo_docker.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService; // Mocked Service ✅

    @InjectMocks
    private UserController userController; // Injecting Mocked Service into Controller ✅

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mocks
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("mySecretPass");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword(passwordEncoder.encode("mySecretPass"));

        when(userService.createUser(any(User.class))).thenReturn(savedUser); // Mock behavior

        // Act
        ResponseEntity<User> response = userController.createUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        assertNotEquals("mySecretPass", response.getBody().getPassword()); // Ensure password is hashed
    }

    @Test
    void testGetUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userService.getUser(1L)).thenReturn(Optional.of(user)); // Mock behavior

        // Act
        ResponseEntity<User> response = userController.getUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L); // Verify method was called once
    }

    @Test
    void testCreateUserWithEmptyPassword() {
        // Arrange
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPassword("");

        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Password cannot be empty"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
    }

    @Test
    void testUpdateUserNotFound() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(anyLong(), any(User.class))).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.updateUser(999L, updatedUser));
    }

    @Test
    void testUpdateUserPasswordHashing() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@example.com");
        user.setPassword("newPassword");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("User");
        updatedUser.setEmail("user@example.com");
        updatedUser.setPassword(passwordEncoder.encode("newPassword"));

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(1L, user);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotEquals("newPassword", response.getBody().getPassword()); // Ensure password is hashed
    }

    @Test
    void testGetUserNotFound() {
        // Arrange
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getUser(999L);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetAllUsersEmptyList() {
        // Arrange
        Page<User> emptyPage = Page.empty();
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<User>> response = userController.getAllUsers(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getContent().isEmpty());
    }

    @Test
    void testDeleteUserNotFound() {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(userService).deleteUser(anyLong());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.deleteUser(999L));
    }

    @Test
    void testDeleteUserSuccessful() {
        // Arrange
        doNothing().when(userService).deleteUser(anyLong());

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        // Arrange
        User user = new User();
        user.setName("John Doe");
        user.setEmail("duplicate@example.com");
        user.setPassword("password123");

        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
    }



}
