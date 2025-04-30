package com.example.authvetclinic.dto;

import com.example.authvetclinic.model.Animal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnimalResponse {
    private Long ownerId;
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String medicalHistory;
    public Animal toAnimal() {
        Animal animal = new Animal();
        animal.setName(this.name);
        animal.setSpecies(this.species);
        animal.setBreed(this.breed);
        animal.setBirthDate(this.birthDate);
        animal.setMedicalHistory(this.medicalHistory);
        return animal;
    }
}
