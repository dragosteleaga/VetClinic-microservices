package com.example.authvetclinic.controller.api;

import com.example.authvetclinic.dto.LoginRequest;
import com.example.authvetclinic.dto.RegisterRequest;
import com.example.authvetclinic.exception.SpecializationRequiredException;
import com.example.authvetclinic.mapper.OwnerMapper;
import com.example.authvetclinic.mapper.UserMapper;
import com.example.authvetclinic.mapper.VeterinarianMapper;
import com.example.authvetclinic.model.*;
import com.example.authvetclinic.service.AuthService;
import com.example.authvetclinic.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthService authService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VeterinarianMapper veterinarianMapper;
    @Autowired
    private OwnerMapper ownerMapper;
    @Autowired
    public JwtUtil jwtUtil;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Attempting to register new user with email: {}", registerRequest.getEmail());
        
        User user = userMapper.toEntity(registerRequest);
        try {
            if (user.getRole() == Role.VETERINARIAN) {
                logger.info("Registering new veterinarian");
                String specialization = registerRequest.getSpecialization();
                if (specialization == null || specialization.trim().isEmpty()) {
                    logger.warn("Registration failed: Specialization required for veterinarian");
                    throw new SpecializationRequiredException("Specialization is required when creating a veterinarian!");
                }

                Veterinarian vet = veterinarianMapper.toEntity(registerRequest);
                Veterinarian savedVet = authService.registerVeterinarian(vet);
                String token = jwtUtil.generateToken(savedVet.getEmail(), user.getRole(), savedVet.getId());
                logger.info("Successfully registered veterinarian with ID: {}", savedVet.getId());
                return new ResponseEntity<>(token, HttpStatus.CREATED);

            } else if (user.getRole() == Role.OWNER) {
                logger.info("Registering new owner");
                Owner owner = ownerMapper.toEntity(registerRequest);
                Owner savedOwner = authService.registerOwner(owner);
                String token = jwtUtil.generateToken(savedOwner.getEmail(), user.getRole(), savedOwner.getId());
                logger.info("Successfully registered owner with ID: {}", savedOwner.getId());
                return new ResponseEntity<>(token, HttpStatus.CREATED);
            }

            User savedUser = authService.registerUser(user);
            String token = jwtUtil.generateToken(savedUser.getEmail(), user.getRole(), savedUser.getId());
            logger.info("Successfully registered user with ID: {}", savedUser.getId());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Registration failed for email: {} - Error: {}", registerRequest.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    @Operation(summary = "Authenticate a user", description = "Logs in an existing user and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        logger.info("Successful login for email: {}", loginRequest.getEmail());
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
}
