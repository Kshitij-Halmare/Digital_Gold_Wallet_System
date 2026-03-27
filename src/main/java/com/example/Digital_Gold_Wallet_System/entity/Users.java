package com.example.Digital_Gold_Wallet_System.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(precision = 18, scale = 2)
    @NotNull
    @PositiveOrZero(message = "Balance should be greater than or equal to 0")
    private BigDecimal balance;
    @Column(columnDefinition = "DATETIME", name="createdAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "addressId")
    private Addresses address;

    @OneToMany(mappedBy = "user")
    private List<VirtualGoldHoldings> virtualGoldHoldings;

    @OneToMany(mappedBy = "user")
    private List<TransactionHistory> transactions;

    @OneToMany(mappedBy = "user")
    private List<Payments> payments;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}
