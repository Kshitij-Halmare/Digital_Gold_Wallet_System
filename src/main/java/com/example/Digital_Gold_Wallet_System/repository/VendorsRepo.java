package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  VendorsRepo extends JpaRepository<Vendors, Integer> {
    List<Vendors> findByVendorName(String vendorName);
}
