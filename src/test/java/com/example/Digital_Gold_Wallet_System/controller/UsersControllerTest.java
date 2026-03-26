package com.example.Digital_Gold_Wallet_System.controller;

import jakarta.transaction.Transactional;
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
    void testGetAllUsers() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
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
    void testGetUserById() throws Exception {

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdateUser() throws Exception {

        String userJson = """
        {
            "email": "updated@gmail.com",
            "name": "UpdatedUser",
            "balance": 2000.00
        }
        """;

        mockMvc.perform(put("/users/1")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUserBalance() throws Exception {

        String userJson = """
        {
            "balance": 2000.00
        }
        """;

        mockMvc.perform(put("/users/2")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/users/2"))
                .andExpect(status().isNoContent());
    }

}
