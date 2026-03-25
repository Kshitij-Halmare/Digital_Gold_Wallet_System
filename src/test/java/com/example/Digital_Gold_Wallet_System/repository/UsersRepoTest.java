package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@SpringBootTest
public class UsersRepoTest {

    @Autowired
    private UsersRepo usersRepo;

    @Test
    public void createUserTest() {

        Users user = new Users();
        user.setName("Kshitij");
        user.setEmail("kshitij@gmail.com");
        user.setBalance(new BigDecimal("1000.50"));
        user.setCreatedAt(LocalDateTime.now());

        Users savedUser = usersRepo.save(user);

        assertNotNull(savedUser.getUserId());
    }
}