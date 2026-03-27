package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@SpringBootTest
public class UsersRepoTest {

    @Autowired
    private UsersRepo usersRepo;

    @Test
    public void createUserTest() {

        Users user = new Users();
        user.setName("Kshitij Halmare");
        user.setEmail("kshitij1@gmail.com");

        Users savedUser = usersRepo.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
    }
}