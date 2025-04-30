package com.example.authvetclinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeterinarianResponse {
    private Long id;
    private String email;
    private String specialization;
    private String firstName;
    private String lastName;
}
