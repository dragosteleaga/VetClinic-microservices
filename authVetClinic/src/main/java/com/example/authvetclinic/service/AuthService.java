package com.example.authvetclinic.service;

import com.example.authvetclinic.exception.UserAlreadyExistsException;
import com.example.authvetclinic.model.Owner;
import com.example.authvetclinic.model.User;
import com.example.authvetclinic.model.Veterinarian;
import com.example.authvetclinic.repository.OwnerRepository;
import com.example.authvetclinic.repository.UserRepository;
import com.example.authvetclinic.repository.VeterinarianRepository;
import com.example.authvetclinic.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    public User registerUser(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public Veterinarian registerVeterinarian(Veterinarian vet) {
        Optional<Veterinarian> existingVet = veterinarianRepository.findByEmail(vet.getEmail());
        if (existingVet.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + vet.getEmail() + " already exists");
        }
        vet.setPassword(passwordEncoder.encode(vet.getPassword()));

        return veterinarianRepository.save(vet);
    }
    public Owner registerOwner(Owner owner) {
        Optional<Owner> existingOwner = ownerRepository.findByEmail(owner.getEmail());
        if (existingOwner.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + owner.getEmail() + " already exists");
        }
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));

        owner.getAnimals().forEach(animal -> animal.setOwner(owner));
        return ownerRepository.save(owner);
    }
    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
    }

}
