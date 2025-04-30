package com.example.authvetclinic.dto;

import com.example.authvetclinic.model.Appointment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OwnerResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<AnimalResponse> animals; // You can return a list of animals associated with this owner
    private List<Appointment>appointments;
}
