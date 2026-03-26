package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class VendorBranches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;
    @NotNull
    @Column(nullable = false,precision = 18, scale = 2)
    private BigDecimal quantity;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendors vendors;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    private Addresses address;

    @OneToMany(mappedBy = "branch")
    private List<TransactionHistory> transactions;

    @OneToMany(mappedBy = "branch")
    private List<VirtualGoldHoldings> holdings;

    @OneToMany(mappedBy = "branch")
    private List<PhysicalGoldTransactions> physicalTransactions;
}

