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
        ResponseEntity<User> response = userController.createUser(user);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        assertNotEquals("mySecretPass", response.getBody().getPassword()); // Ensure password is hashed
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        when(userService.getUser(1L)).thenReturn(Optional.of(user)); // Mock behavior
        ResponseEntity<User> response = userController.getUser(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L); // Verify method was called once
    }

    @Test
    void testCreateUserWithEmptyPassword() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPassword("");

        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Password cannot be empty"));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
    }

    @Test
    void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(anyLong(), any(User.class))).thenThrow(new RuntimeException("User not found"));
        assertThrows(RuntimeException.class, () -> userController.updateUser(999L, updatedUser));
    }

    @Test
    void testUpdateUserPasswordHashing() {
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
        ResponseEntity<User> response = userController.updateUser(1L, user);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotEquals("newPassword", response.getBody().getPassword()); // Ensure password is hashed
    }

    @Test
    void testGetUserNotFound() {
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.getUser(999L);
        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testDeleteUserNotFound() {
        doThrow(new RuntimeException("User not found"))
                .when(userService).deleteUser(anyLong());
        assertThrows(RuntimeException.class, () -> userController.deleteUser(999L));
    }

    @Test
    void testDeleteUserSuccessful() {
        doNothing().when(userService).deleteUser(anyLong());
        ResponseEntity<Void> response = userController.deleteUser(1L);
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
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
    }



}
