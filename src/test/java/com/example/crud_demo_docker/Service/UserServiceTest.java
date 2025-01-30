package com.example.crud_demo_docker.Service;

import com.example.crud_demo_docker.Controller.UserController;
import com.example.crud_demo_docker.Entity.User;
import com.example.crud_demo_docker.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(); // Initialize encoder
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("mySecretPass");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword(passwordEncoder.encode("mySecretPass")); // Hashed password

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        ResponseEntity<User> response = userController.createUser(user);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        assertNotEquals("mySecretPass", response.getBody().getPassword()); // Ensuring password is hashed
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Updated Name");
        user.setEmail("updated@example.com");
        user.setPassword("newSecretPass");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword(passwordEncoder.encode("oldPassword"));

        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
        assertNotEquals("newSecretPass", response.getBody().getPassword()); // Ensuring password is hashed
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");

        when(userService.getUser(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUser(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        Page<User> users = new PageImpl<>(Collections.singletonList(user));

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(users);

        ResponseEntity<Page<User>> response = userController.getAllUsers(0, 10);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("John Doe", response.getBody().getContent().get(0).getName());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }
}
