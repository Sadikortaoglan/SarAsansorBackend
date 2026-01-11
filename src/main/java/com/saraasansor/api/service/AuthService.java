package com.saraasansor.api.service;

import com.saraasansor.api.dto.auth.LoginRequest;
import com.saraasansor.api.dto.auth.LoginResponse;
import com.saraasansor.api.dto.auth.RegisterRequest;
import com.saraasansor.api.model.User;
import com.saraasansor.api.repository.UserRepository;
import com.saraasansor.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String accessToken = tokenProvider.generateAccessToken(
            user.getUsername(), user.getId(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());
        
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        
        return response;
    }
    
    public LoginResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken, tokenProvider.getUsernameFromToken(refreshToken))) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String newAccessToken = tokenProvider.generateAccessToken(
            user.getUsername(), user.getId(), user.getRole().name());
        
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        
        return response;
    }
    
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("This username is already in use");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        try {
            user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Must be PATRON or PERSONEL.");
        }
        
        user.setActive(true);
        User savedUser = userRepository.save(user);
        
        // Auto login
        String accessToken = tokenProvider.generateAccessToken(
            savedUser.getUsername(), savedUser.getId(), savedUser.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(savedUser.getUsername());
        
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole().name());
        
        return response;
    }
}

