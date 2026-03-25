package com.example.Digital_Gold_Wallet_System.controller;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

@Transactional
public class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testAddNewUserPositive() throws Exception{
        String userAddJson = """
        {
            "name": "Pratham",
            "email": "pratham@gmail.com"
        }
        """;

    mockMvc.perform(post("/users/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userAddJson))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Pratham"))
            .andExpect(jsonPath("$.email").value("pratham@gmail.com"));
            
    }


}
