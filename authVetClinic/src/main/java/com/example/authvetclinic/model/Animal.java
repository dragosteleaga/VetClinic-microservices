package com.example.authvetclinic.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String medicalHistory;
}
