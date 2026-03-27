package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
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

    @Autowired
    private UsersRepo usersRepository;

    private Users createTestUser() {
        Users user = new Users();
        user.setName("TestUser");
        user.setEmail("test@gmail.com");
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
    @DisplayName("Test Get All Users When DB Is Empty")
    void testGetAllUsersEmpty() throws Exception {

        usersRepository.deleteAll();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
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
    @DisplayName("Test Create User With Duplicate Email ID")
    void testCreateUserDuplicateEmail() throws Exception {

        createTestUser();

        String duplicateUserJson = """
    {
        "name": "AnotherUser",
        "email": "test@gmail.com",
    }
    """;

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(duplicateUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Find By Email")
    void testFindUserByEmail() throws Exception {

        mockMvc.perform(get("/users/search/findByEmailContainingIgnoreCase")
                        .param("email",createTestUser().getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test By Non-Existing Email")
    void testFindUserByEmailNotFound() throws Exception {

        mockMvc.perform(get("/users/search/findByEmailContainingIgnoreCase")
                        .param("email", "nonexisting@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
    }

    @Test
    @DisplayName("Test By Non-Existing Name")
    void testFindUserByNameNotFound() throws Exception {

        mockMvc.perform(get("/users/search/findByNameContainingIgnoreCase")
                        .param("name", "UnknownUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userses").isEmpty());
    }

    @Test
    @DisplayName("Test Find By Name")
    void testFindUserByName() throws Exception {

        mockMvc.perform(get("/users/search/findByNameContainingIgnoreCase")
                        .param("name",createTestUser().getName()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test User NOT Found")
    void testUserNotFound() throws Exception {

        mockMvc.perform(get("/users/9999"))
                .andExpect(status().isBadRequest());
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
    @DisplayName("Test Update User When UserID Doesn't Exist")
    void testUpdateUserInvalidId() throws Exception {

        String updatedJson = """
    {
        "name": "UpdatedUser",
        "email": "updated@gmail.com",
        "balance": 2000
    }
    """;

        mockMvc.perform(put("/users/9999")
                        .contentType("application/json")
                        .content(updatedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Delete User")
    void testDeleteUser() throws Exception {

        Users savedUser = createTestUser();

        mockMvc.perform(delete("/users/" + savedUser.getUserId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test Delete User When UserID Doesn't Exist")
    void testDeleteUserInvalidId() throws Exception {

        mockMvc.perform(delete("/users/9999"))
                .andExpect(status().isBadRequest());
    }

}
