package com.example.Digital_Gold_Wallet_System.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VirtualGoldHoldings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holdingId;

    @Column(precision = 18, scale = 2)
    private BigDecimal quantity;
    @Column(columnDefinition = "DATETIME",name = "createdAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "branchId")
    private VendorBranches branch;

}
