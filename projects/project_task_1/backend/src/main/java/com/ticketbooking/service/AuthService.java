package com.ticketbooking.service;

import com.ticketbooking.dto.LoginRequest;
import com.ticketbooking.dto.LoginResponse;
import com.ticketbooking.dto.RegisterRequest;
import com.ticketbooking.model.User;
import com.ticketbooking.repository.UserRepository;
import com.ticketbooking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> register(RegisterRequest request, String callerRole) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken.");
        }

        User.Role role = User.Role.student;
        if (request.getRole() != null) {
            try {
                role = User.Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                role = User.Role.student;
            }
        }

        if (role == User.Role.admin) {
            if (!"admin".equals(callerRole)) {
                throw new RuntimeException("Only existing admins can create new admins.");
            }
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setDepartment(request.getDepartment());

        User saved = userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "User registered successfully!");
        result.put("id", saved.getId());
        return result;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().name());
        claims.put("name", user.getName());
        claims.put("department", user.getDepartment());

        String token = jwtUtil.generateToken(claims, user.getEmail());

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("role", user.getRole().name());
        userData.put("email", user.getEmail());
        userData.put("department", user.getDepartment());

        return new LoginResponse(token, userData);
    }
}
