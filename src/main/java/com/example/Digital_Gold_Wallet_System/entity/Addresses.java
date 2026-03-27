package com.example.Digital_Gold_Wallet_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

//    @NotBlank
    private String street;
//    @NotBlank
    private String city;
//    @NotBlank
    private String state;

    @Column(name = "postalCode")
//    @NotBlank
    private String postalCode;

//    @NotBlank
    private String country;

    @OneToMany(mappedBy = "address")
    private List<Users> users;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<VendorBranches> vendorBranches;
}