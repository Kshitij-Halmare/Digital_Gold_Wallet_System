package com.example.Digital_Gold_Wallet_System.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String email;
    private String name;

    @Column(precision = 18, scale = 2)
    private BigDecimal balance;
    @Column(columnDefinition = "DATETIME", name="createdAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "addressId")
    private Addresses addresses;

    @OneToMany(mappedBy = "user")
    private List<VirtualGoldHoldings> virtualGoldHoldings;

    @OneToMany(mappedBy = "user")
    private List<TransactionHistory> transactions;

    @OneToMany(mappedBy = "user")
    private List<Payments> payments;

}
