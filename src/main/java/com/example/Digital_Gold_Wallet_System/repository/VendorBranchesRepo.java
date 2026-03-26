package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "branches")
public interface VendorBranchesRepo extends JpaRepository<VendorBranches, Integer> {

//    List<VendorBranches> findByAddressessCity(String city);
//    List<VendorBranches> findByAddressessState(String state);
//    List<VendorBranches> findByAddressessCountry(String country);
//    List<VendorBranches> findByAddressessPostalCode(String postalCode);
//    List<VendorBranches> findByQuantityBetween(BigDecimal min, BigDecimal max);
//    List<VendorBranches> findByAddressessCityAndAddressessState(String city, String state);
//    List<VendorBranches> findByAddressessStateAndAddressessCountry(String state, String country);
//    List<VendorBranches> findByAddressessCityContainingIgnoreCase(String city);
//    List<VendorBranches> findByAddressessStateContainingIgnoreCase(String state);
//    List<VendorBranches> findByAddressessCityOrderByQuantityAsc(String city);
//    List<VendorBranches> findByAddressessCityOrderByQuantityDesc(String city);
}