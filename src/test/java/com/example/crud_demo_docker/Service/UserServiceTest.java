package com.example.crud_demo_docker.Service;

import com.example.crud_demo_docker.Entity.User;
import com.example.crud_demo_docker.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("password");
    }

    @Test
    void createUser() {
        when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        verify(userRepository).save(user);
        assertEquals(user, createdUser);
    }

    @Test
    void updateUser() {
        // Assuming that the user with id 1 exists in the repository
        user.setId(1L);
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        // Mock the behavior of finding the user and saving the updated user
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        User result = userService.updateUser(user.getId(), updatedUser);
        verify(userRepository).findById(user.getId());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        // Assert that the captured user object is the updated one
        User capturedUser = userCaptor.getValue();
        assertEquals("Updated Name", capturedUser.getName());
        assertEquals("updated@example.com", capturedUser.getEmail());
        assertEquals("newpassword", capturedUser.getPassword());

        //  updated values
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("newpassword", result.getPassword());
    }

    @Test
    void deleteUser() {
        user.setId(1L);
        when(userRepository.existsById(user.getId())).thenReturn(true);
        userService.deleteUser(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void getUserById() {
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        User fetchedUser = userService.getUserById(user.getId());
        verify(userRepository).findById(user.getId());
        assertEquals(user, fetchedUser);
    }

    @Test
    void getUserByIdNotFound() {
        user.setId(2L);
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.empty());
        User fetchedUser = userService.getUserById(user.getId());
        verify(userRepository).findById(user.getId());
        assertNull(fetchedUser);
    }
}
