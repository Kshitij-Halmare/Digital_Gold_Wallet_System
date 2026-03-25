package com.example.Digital_Gold_Wallet_System.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String name;

    // Big Decimal handles floating point issues better than Double.
    @Column(precision = 18, scale = 2)
    private BigDecimal balance; 

    @Column(columnDefinition = "DATETIME", name = "created_at")
    private LocalDateTime createdAt;

}
