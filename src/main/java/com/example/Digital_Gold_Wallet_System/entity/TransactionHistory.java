package com.example.Digital_Gold_Wallet_System.entity;

import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull
    @PositiveOrZero
    @Column(precision = 10,scale = 2)
    private BigDecimal quantity;

    @NotNull
    @PositiveOrZero
    @Column(precision = 18,scale=2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "branchId")
    private VendorBranches branch;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

}
