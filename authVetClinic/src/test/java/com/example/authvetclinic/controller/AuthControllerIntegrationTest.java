package com.example.authvetclinic.controller;

import com.example.authvetclinic.dto.RegisterRequest;
import com.example.authvetclinic.model.Role;
import com.example.authvetclinic.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/data.sql")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterAdminEndpoint() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("newadmin@example.com");
        request.setPassword("mypassword");
        request.setFirstName("Mia");
        request.setLastName("Wallace");
        request.setRole(Role.ADMIN);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("eyJ"))); // JWT starts with this
    }

    @Test
    void testRegisterVetMissingSpecializationShouldFail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("vet1@example.com");
        request.setPassword("securepass");
        request.setFirstName("Doc");
        request.setLastName("Strange");
        request.setRole(Role.VETERINARIAN);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Specialization is required")));
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        String json = """
                {
                  "email": "admin2@admin.com",
                  "password": "asdasdasd"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("eyJ")));
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        String json = """
                {
                  "email": "admin@admin.com",
                  "password": "wrong"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println("Response body: " + result.getResponse().getContentAsString());
    }
}