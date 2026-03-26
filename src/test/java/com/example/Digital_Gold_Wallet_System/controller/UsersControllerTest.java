package com.example.Digital_Gold_Wallet_System.controller;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepo usersRepository;

    private Users createTestUser() {
        Users user = new Users();
        user.setName("TestUser");
        user.setEmail("test@gmail.com");
        user.setBalance(BigDecimal.valueOf(1000));
        return usersRepository.save(user);
    }

    @Test
    @DisplayName("Test Get All Users")
    void testGetAllUsers() throws Exception {

        createTestUser();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Create User")
    void testCreateUser() throws Exception {

        String userJson = """
        {
            "name": "Pratham",
            "email": "pratham@gmail.com",
            "balance": 5000
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test Find User By ID")
    void testGetUserById() throws Exception {

        Users savedUser = createTestUser();

        mockMvc.perform(get("/users/" + savedUser.getUserId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Update User")
    void testUpdateUser() throws Exception {

        Users savedUser = createTestUser();

        String updatedJson = """
        {
            "userName": "UpdatedUser",
            "userEmail": "updated@gmail.com",
            "userBalance": 2000
        }
        """;

        mockMvc.perform(put("/users/" + savedUser.getUserId())
                        .contentType("application/json")
                        .content(updatedJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test Delete User")
    void testDeleteUser() throws Exception {

        Users savedUser = createTestUser();

        mockMvc.perform(delete("/users/" + savedUser.getUserId()))
                .andExpect(status().isNoContent());
    }

}
