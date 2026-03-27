package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PhysicalGoldTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @NotNull
    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(columnDefinition = "DATETIME", name = "createdAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "branchId")
    private VendorBranches branch;

    @ManyToOne
    @JoinColumn(name = "deliveryAddressId")
    private Addresses deliveryAddress;
}
