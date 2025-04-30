package com.example.authvetclinic.mapper;

import com.example.authvetclinic.dto.RegisterRequest;
import com.example.authvetclinic.dto.VeterinarianResponse;
import com.example.authvetclinic.model.Role;
import com.example.authvetclinic.model.Veterinarian;
import org.springframework.stereotype.Component;

@Component
public class VeterinarianMapper {
    public Veterinarian toEntity(RegisterRequest registerRequest) {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setEmail(registerRequest.getEmail());
        veterinarian.setPassword(registerRequest.getPassword());
        veterinarian.setFirstName(registerRequest.getFirstName());
        veterinarian.setLastName(registerRequest.getLastName());
        veterinarian.setRole(Role.VETERINARIAN);
        veterinarian.setSpecialization(registerRequest.getSpecialization());
        return veterinarian;
    }
    public VeterinarianResponse toResponse(Veterinarian vet) {
        VeterinarianResponse response = new VeterinarianResponse();
        response.setId(vet.getId());
        response.setEmail(vet.getEmail());
        response.setFirstName(vet.getFirstName());
        response.setLastName(vet.getLastName());
        response.setSpecialization(vet.getSpecialization());
        return response;
    }
}
