package com.saraasansor.api.controller;

import com.saraasansor.api.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username is required"));
            }
            
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already exists"));
            }
            
            // Password hash is required for new users
            if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Password is required"));
            }
            
            // Encode password
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            
            // Set defaults
            if (user.getRole() == null) {
                user.setRole(User.Role.PERSONEL);
            }
            if (user.getActive() == null) {
                user.setActive(true);
            }
            
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
            @PathVariable Long id, @Valid @RequestBody User user) {
        try {
            Optional<User> existingUserOpt = userRepository.findById(id);
            if (!existingUserOpt.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User not found"));
            }
            
            User userToUpdate = existingUserOpt.get();
            
            // Update username if changed
            if (user.getUsername() != null && !user.getUsername().isEmpty() && 
                !user.getUsername().equals(userToUpdate.getUsername())) {
                if (userRepository.existsByUsername(user.getUsername())) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Username already exists"));
                }
                userToUpdate.setUsername(user.getUsername());
            }
            
            // Update password ONLY if provided (not empty)
            // If passwordHash is provided in request, it means user wants to change password
            // We need to check if it's a plain password (needs encoding) or already hashed
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                // Check if it's already a BCrypt hash (starts with $2a$)
                if (user.getPasswordHash().startsWith("$2a$") || user.getPasswordHash().startsWith("$2b$")) {
                    // Already hashed, use as is (but this is unusual - usually we encode plain passwords)
                    userToUpdate.setPasswordHash(user.getPasswordHash());
                } else {
                    // Plain password, encode it
                    userToUpdate.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
                }
            }
            // If passwordHash is null or empty, don't update it (keep existing password)
            
            // Update role if provided
            if (user.getRole() != null) {
                userToUpdate.setRole(user.getRole());
            }
            
            // Update active status if provided
            if (user.getActive() != null) {
                userToUpdate.setActive(user.getActive());
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
