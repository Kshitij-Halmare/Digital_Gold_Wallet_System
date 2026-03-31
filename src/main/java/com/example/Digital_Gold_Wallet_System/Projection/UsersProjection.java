package com.example.Digital_Gold_Wallet_System.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UsersProjection {
    Integer getUserId();
    String getName();
    String getEmail();
    BigDecimal getBalance();
    LocalDateTime getCreatedAt();
}
