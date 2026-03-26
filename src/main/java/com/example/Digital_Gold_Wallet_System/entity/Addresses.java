package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    private String street;
    private String city;
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

//    @OneToMany(mappedBy = "addressess", cascade = CascadeType.ALL)
//    private List<VendorBranches> vendorBranches;
}