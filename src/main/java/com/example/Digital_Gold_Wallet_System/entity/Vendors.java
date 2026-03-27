package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "vendors")   // ✅ FIXED
public class Vendors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")   // ✅ good practice
    private Integer vendorId;

    @NotBlank
    private String vendorName;

    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;

    @NotNull
    @PositiveOrZero
    @Column(precision = 18, scale = 2)
    private BigDecimal totalGoldQuantity;

    @NotNull
    @PositiveOrZero
    @Column(precision = 18, scale = 2)
    private BigDecimal currentGoldPrice;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "vendors", cascade = CascadeType.ALL)
    private List<VendorBranches> branches;
}