package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class VendorBranches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;
    @Column(precision = 18, scale = 2)
    private BigDecimal quantity;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressess_address_id")
    private Addresses addressess;

    @ManyToOne
    private Vendors vendors;
}

