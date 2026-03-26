package com.example.Digital_Gold_Wallet_System.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test Get All")
    void testGetAllUsers() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Create User")
    void testCreateUser() throws Exception {

        String userJson = """
        {
            "email": "test@gmail.com",
            "name": "Pratham"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test Find by ID")
    void testGetUserById() throws Exception {

        mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Test Update User")
    void testUpdateUser() throws Exception {

        String userJson = """
        {
            "email": "updated@gmail.com",
            "name": "UpdatedUser",
            "balance": 2000.00
        }
        """;

        mockMvc.perform(put("/users/2")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test Delete User")
    void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/users/2"))
                .andExpect(status().isNoContent());
    }

}
