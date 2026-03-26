package com.example.Digital_Gold_Wallet_System.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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


}
