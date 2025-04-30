package com.example.authvetclinic.mapper;

import com.example.authvetclinic.dto.AnimalResponse;
import com.example.authvetclinic.dto.OwnerResponse;
import com.example.authvetclinic.dto.RegisterRequest;
import com.example.authvetclinic.model.Owner;
import com.example.authvetclinic.model.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class OwnerMapper {
    public Owner toEntity(RegisterRequest registerRequest){
        Owner owner=new Owner();
        owner.setEmail(registerRequest.getEmail());
        owner.setPassword(registerRequest.getPassword());
        owner.setFirstName(registerRequest.getFirstName());
        owner.setLastName(registerRequest.getLastName());
        owner.setRole(Role.OWNER);
        // Defensive null check for animals
        if (registerRequest.getAnimals() != null) {
            owner.setAnimals(registerRequest.getAnimals());
        } else {
            owner.setAnimals(new ArrayList<>()); // 👈 prevents the null issue
        }
        return owner;
    }
    public OwnerResponse toResponse(Owner owner) {
        OwnerResponse response = new OwnerResponse();
        response.setId(owner.getId());
        response.setEmail(owner.getEmail());
        response.setFirstName(owner.getFirstName());
        response.setLastName(owner.getLastName());
        response.setAppointments(owner.getAppointments());

        // Mapping the animals associated with the owner
        response.setAnimals(owner.getAnimals().stream()
                .map(animal -> {
                    AnimalResponse animalResponse = new AnimalResponse();
                    animalResponse.setId(animal.getId());
                    animalResponse.setName(animal.getName());
                    animalResponse.setSpecies(animal.getSpecies());
                    animalResponse.setBreed(animal.getBreed());
                    animalResponse.setBirthDate(animal.getBirthDate());
                    animalResponse.setMedicalHistory(animal.getMedicalHistory());
                    return animalResponse;
                }).collect(Collectors.toList()));

        return response;
    }
}
