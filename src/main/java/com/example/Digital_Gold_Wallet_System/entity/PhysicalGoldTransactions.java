package com.example.Digital_Gold_Wallet_System.entity;

<<<<<<< HEAD
public class PhysicalGoldTransactions {

=======
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class PhysicalGoldTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id;
    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
>>>>>>> 14f0dd6b1796064b9098d424ac5335349a7a2b53
}
