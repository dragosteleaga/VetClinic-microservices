package com.example.authvetclinic.repository;

import com.example.authvetclinic.model.Appointment;
import com.example.authvetclinic.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    Optional<Veterinarian> findByEmail(String email);
    @Query("SELECT v.appointments FROM Veterinarian v WHERE v.id = :id")
    List<Appointment> findAppointmentsByVeterinarian(Long id);
}
