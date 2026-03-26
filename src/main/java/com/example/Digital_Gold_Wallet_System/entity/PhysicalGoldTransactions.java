package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PhysicalGoldTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;
    @Column(columnDefinition = "DATETIME", name = "created_at")
    private LocalDateTime createdAt;
}
