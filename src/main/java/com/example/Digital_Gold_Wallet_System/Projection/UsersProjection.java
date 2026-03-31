package com.example.Digital_Gold_Wallet_System.Projection;

import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "userProjection", types = {UsersProjection.class})
public interface UsersProjection {
    Integer getUserId();
    String getName();
    String getEmail();
    BigDecimal getBalance();
    LocalDateTime getCreatedAt();
}
