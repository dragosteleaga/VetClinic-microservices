package com.example.authvetclinic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToOne(mappedBy = "recipe")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToMany
    @JoinTable(
            name = "recipe_drugs",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id")
    )
    private List<Drug> drugs;
}
