package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class VendorBranches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;
    @Column(precision = 18, scale = 2)
    private BigDecimal quantity;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

}

