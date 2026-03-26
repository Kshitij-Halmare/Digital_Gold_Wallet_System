package com.example.Digital_Gold_Wallet_System.entity;

import com.example.Digital_Gold_Wallet_System.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.enums.PaymentTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    @Enumerated(EnumType.STRING)
    private PaymentTransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @Column(precision = 10,scale = 2)
    private BigDecimal quantity;
    @Column(precision = 18,scale=2)
    private BigDecimal amount;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;





}
