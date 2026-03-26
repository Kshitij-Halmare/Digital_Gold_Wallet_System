package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;

import java.util.List;

@Entity
@Data
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Integer addressId;

    private String street;
    private String city;
    private String state;

    @Column(name = "postalCode")
    private String postalCode;

    private String country;

    @OneToMany(mappedBy = "addresses")
    private List<Users> users;

    @OneToMany(mappedBy = "addresses", cascade = CascadeType.ALL)
    private List<VendorBranches> vendorBranches;
}