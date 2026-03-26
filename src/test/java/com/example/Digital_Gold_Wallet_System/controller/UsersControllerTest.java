package com.example.Digital_Gold_Wallet_System.controller;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
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
    UsersRepo usersRepo;
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

        Users user = new Users();
        user.setEmail("test@gmail.com");
        user.setName("Pratham");

        Users savedUser = usersRepo.save(user);

        mockMvc.perform(get("/users/" + savedUser.getUserId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }


}
