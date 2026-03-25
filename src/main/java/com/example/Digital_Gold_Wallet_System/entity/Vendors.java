package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Vendors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vendorId;
    private String vendorName;
    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;
    @Column(precision = 18, scale = 2)
    private BigDecimal totalGoldQuantity;
    @Column(precision = 18, scale = 2)
    private BigDecimal currentGoldPrice;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

}
