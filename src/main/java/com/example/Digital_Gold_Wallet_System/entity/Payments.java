package com.example.Digital_Gold_Wallet_System.entity;

import com.example.Digital_Gold_Wallet_System.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.enums.PaymentTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer payment_id;

    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private PaymentTransactionType paymentTransactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
