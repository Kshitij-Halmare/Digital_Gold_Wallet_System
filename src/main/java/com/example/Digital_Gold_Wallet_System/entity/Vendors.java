package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "vendors", cascade = CascadeType.ALL)
    private List<VendorBranches> vendorBranches;
}