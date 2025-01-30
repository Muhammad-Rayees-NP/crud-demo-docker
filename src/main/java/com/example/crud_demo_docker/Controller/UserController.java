package com.example.crud_demo_docker.Controller;


import com.example.crud_demo_docker.Entity.User;
import com.example.crud_demo_docker.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder().encode(user.getPassword()));

        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Create a PasswordEncoder bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Operation(summary = " Get a user")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all users with pagination and its by default no")
    @GetMapping
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = " Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
