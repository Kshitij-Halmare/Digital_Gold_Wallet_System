package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name="userList", types=Users.class)
public interface UserListProjection {

    Integer getUserId();
    String getName();
    String getEmail();
    BigDecimal getBalance();
    LocalDateTime getCreatedAt();
}