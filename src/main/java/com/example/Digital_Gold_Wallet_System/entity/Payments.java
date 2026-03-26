package com.example.Digital_Gold_Wallet_System.entity;

import com.example.Digital_Gold_Wallet_System.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.enums.PaymentTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private Integer paymentId;
    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "transactionType")
    private PaymentTransactionType paymentTransactionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    private PaymentStatus paymentStatus;
    @Column(name = "createdAt", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "usersId")
    private Users user;

}
