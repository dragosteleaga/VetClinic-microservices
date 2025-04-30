package com.example.authvetclinic.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("VETERINARIAN")
@Getter
@Setter
public class Veterinarian extends User {
    private String specialization;  // Specific attribute for veterinarians
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

}