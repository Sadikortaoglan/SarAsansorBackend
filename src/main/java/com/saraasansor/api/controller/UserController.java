package com.saraasansor.api.controller;

import com.saraasansor.api.dto.ApiResponse;
import com.saraasansor.api.dto.UserRequestDto;
import com.saraasansor.api.model.User;
import com.saraasansor.api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequestDto dto) {
        try {
            // Validate password is required for CREATE
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Password is required when creating a new user"));
            }
            
            if (userRepository.existsByUsername(dto.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already exists"));
            }
            
            // Create new user
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            
            if (dto.getRole() != null) {
                user.setRole(dto.getRole());
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Role is required"));
            }
            
            user.setActive(dto.getActive() != null ? dto.getActive() : true);
            
            User saved = userRepository.save(user);
            // Don't return password hash
            saved.setPasswordHash(null);
            return ResponseEntity.ok(ApiResponse.success("User successfully created", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id, @RequestBody UserRequestDto dto) {
        try {
            Optional<User> existingUserOpt = userRepository.findById(id);
            if (!existingUserOpt.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found"));
            }
            
            User userToUpdate = existingUserOpt.get();
            
            // Update username if provided and changed
            if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty() && 
                !dto.getUsername().equals(userToUpdate.getUsername())) {
                if (userRepository.existsByUsername(dto.getUsername())) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Username already exists"));
                }
                userToUpdate.setUsername(dto.getUsername());
            }
            
            // Update password ONLY if provided and not blank
            // If password is null, empty, or blank → keep existing password
            if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
                userToUpdate.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            }
            // No else - password is optional, existing password is preserved
            
            // Update role if provided
            if (dto.getRole() != null) {
                userToUpdate.setRole(dto.getRole());
            }
            
            // Update active status if provided
            if (dto.getActive() != null) {
                userToUpdate.setActive(dto.getActive());
            }
            
            User saved = userRepository.save(userToUpdate);
            // Don't return password hash
            saved.setPasswordHash(null);
            return ResponseEntity.ok(ApiResponse.success("User successfully updated", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found"));
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("User successfully deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
