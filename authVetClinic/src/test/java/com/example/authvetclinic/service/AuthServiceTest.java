package com.example.authvetclinic.service;

import com.example.authvetclinic.exception.UserAlreadyExistsException;
import com.example.authvetclinic.model.*;
import com.example.authvetclinic.repository.OwnerRepository;
import com.example.authvetclinic.repository.UserRepository;
import com.example.authvetclinic.repository.VeterinarianRepository;
import com.example.authvetclinic.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User user;
    private Veterinarian veterinarian;
    private Owner owner;
    private Animal animal;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String ENCODED_PASSWORD = "encodedPassword";
    private final String JWT_TOKEN = "jwtToken";

    @BeforeEach
    void setUp() {
        // Setup basic user
        user = new User();
        user.setId(1L);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.ADMIN);

        // Setup veterinarian
        veterinarian = new Veterinarian();
        veterinarian.setId(2L);
        veterinarian.setEmail("vet@example.com");
        veterinarian.setPassword(TEST_PASSWORD);
        veterinarian.setFirstName("Test");
        veterinarian.setLastName("Veterinarian");
        veterinarian.setRole(Role.VETERINARIAN);
        veterinarian.setSpecialization("Surgery");

        // Setup animal
        animal = new Animal();
        animal.setId(1L);
        animal.setName("Max");
        animal.setSpecies("Dog");
        animal.setBreed("Golden Retriever");

        // Setup owner with animal
        owner = new Owner();
        owner.setId(3L);
        owner.setEmail("owner@example.com");
        owner.setPassword(TEST_PASSWORD);
        owner.setFirstName("Test");
        owner.setLastName("Owner");
        owner.setRole(Role.OWNER);
        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        owner.setAnimals(animals);

        // Mock password encoder
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        // Mock JWT token generation
        when(jwtUtil.generateToken(anyString(), any(), any())).thenReturn(JWT_TOKEN);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = authService.registerUser(user);

        assertNotNull(savedUser);
        assertEquals(TEST_EMAIL, savedUser.getEmail());
        assertEquals(ENCODED_PASSWORD, savedUser.getPassword());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterVeterinarian_Success() {
        when(veterinarianRepository.findByEmail("vet@example.com")).thenReturn(Optional.empty());
        when(veterinarianRepository.save(any(Veterinarian.class))).thenReturn(veterinarian);

        Veterinarian savedVet = authService.registerVeterinarian(veterinarian);

        assertNotNull(savedVet);
        assertEquals("vet@example.com", savedVet.getEmail());
        assertEquals(ENCODED_PASSWORD, savedVet.getPassword());
        assertEquals("Surgery", savedVet.getSpecialization());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(veterinarianRepository).save(veterinarian);
    }

    @Test
    void testRegisterVeterinarian_AlreadyExists() {
        when(veterinarianRepository.findByEmail("vet@example.com")).thenReturn(Optional.of(veterinarian));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerVeterinarian(veterinarian);
        });

        verify(veterinarianRepository, never()).save(any(Veterinarian.class));
    }

    @Test
    void testRegisterOwner_Success() {
        when(ownerRepository.findByEmail("owner@example.com")).thenReturn(Optional.empty());
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        Owner savedOwner = authService.registerOwner(owner);

        assertNotNull(savedOwner);
        assertEquals("owner@example.com", savedOwner.getEmail());
        assertEquals(ENCODED_PASSWORD, savedOwner.getPassword());
        assertEquals(1, savedOwner.getAnimals().size());
        assertEquals(owner, savedOwner.getAnimals().get(0).getOwner());
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(ownerRepository).save(owner);
    }

    @Test
    void testRegisterOwner_AlreadyExists() {
        when(ownerRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(owner));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerOwner(owner);
        });

        verify(ownerRepository, never()).save(any(Owner.class));
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);

        String token = authService.login(TEST_EMAIL, TEST_PASSWORD);

        assertNotNull(token);
        assertEquals(JWT_TOKEN, token);
        verify(jwtUtil).generateToken(TEST_EMAIL, Role.ADMIN, 1L);
    }

    @Test
    void testLogin_InvalidEmail() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(TEST_EMAIL, TEST_PASSWORD);
        });

        verify(jwtUtil, never()).generateToken(anyString(), any(), any());
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(TEST_EMAIL, TEST_PASSWORD);
        });

        verify(jwtUtil, never()).generateToken(anyString(), any(), any());
    }

    @Test
    void testLogin_WithVeterinarian() {
        when(userRepository.findByEmail("vet@example.com")).thenReturn(Optional.of(veterinarian));
        when(passwordEncoder.matches(TEST_PASSWORD, veterinarian.getPassword())).thenReturn(true);

        String token = authService.login("vet@example.com", TEST_PASSWORD);

        assertNotNull(token);
        assertEquals(JWT_TOKEN, token);
        verify(jwtUtil).generateToken("vet@example.com", Role.VETERINARIAN, 2L);
    }

    @Test
    void testLogin_WithOwner() {
        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(owner));
        when(passwordEncoder.matches(TEST_PASSWORD, owner.getPassword())).thenReturn(true);

        String token = authService.login("owner@example.com", TEST_PASSWORD);

        assertNotNull(token);
        assertEquals(JWT_TOKEN, token);
        verify(jwtUtil).generateToken("owner@example.com", Role.OWNER, 3L);
    }
}