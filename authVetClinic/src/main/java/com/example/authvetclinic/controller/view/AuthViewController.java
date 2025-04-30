package com.example.authvetclinic.controller.view;

import com.example.authvetclinic.dto.LoginRequest;
import com.example.authvetclinic.dto.RegisterRequest;
import com.example.authvetclinic.exception.SpecializationRequiredException;
import com.example.authvetclinic.mapper.OwnerMapper;
import com.example.authvetclinic.mapper.UserMapper;
import com.example.authvetclinic.mapper.VeterinarianMapper;
import com.example.authvetclinic.model.Owner;
import com.example.authvetclinic.model.Role;
import com.example.authvetclinic.model.User;
import com.example.authvetclinic.model.Veterinarian;
import com.example.authvetclinic.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VeterinarianMapper veterinarianMapper;
    @Autowired
    private OwnerMapper ownerMapper;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // src/main/resources/templates/login.html
    }

    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute("loginRequest") @Valid LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            String token = authService.login(loginRequest.getEmail(),loginRequest.getPassword());
            request.getSession().setAttribute("jwtToken", token);
            return "redirect:/owner/dashboard"; // Redirect after successful login
        }  catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register"; // src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("registerRequest") @Valid RegisterRequest registerRequest,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            User user = userMapper.toEntity(registerRequest);
            if (user.getRole() == Role.VETERINARIAN) {
                String specialization = registerRequest.getSpecialization();
                if (specialization == null || specialization.trim().isEmpty()) {
                    model.addAttribute("error", "Specialization is required for veterinarians.");
                    return "register";
                }
                Veterinarian vet = veterinarianMapper.toEntity(registerRequest);
                authService.registerVeterinarian(vet);
            } else if (user.getRole() == Role.OWNER) {
                Owner owner = ownerMapper.toEntity(registerRequest);
                authService.registerOwner(owner);
            } else {
                authService.registerUser(user);
            }

            return "redirect:/auth/login"; // Redirect after successful registration
        } catch (SpecializationRequiredException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
