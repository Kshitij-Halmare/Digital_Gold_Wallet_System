package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "branches")
public interface VendorBranchesRepo extends JpaRepository<VendorBranches, Integer> {

    List<VendorBranches> findByAddressesCity(String city);
    List<VendorBranches> findByAddressesState(String state);
    List<VendorBranches> findByAddressesCountry(String country);
    List<VendorBranches> findByAddressesPostalCode(String postalCode);
    List<VendorBranches> findByQuantityBetween(BigDecimal min, BigDecimal max);
    List<VendorBranches> findByAddressesCityAndAddressesState(String city, String state);
    List<VendorBranches> findByAddressesStateAndAddressesCountry(String state, String country);
    List<VendorBranches> findByAddressesCityContainingIgnoreCase(String city);
    List<VendorBranches> findByAddressesStateContainingIgnoreCase(String state);
    List<VendorBranches> findByAddressesCityOrderByQuantityAsc(String city);
    List<VendorBranches> findByAddressesCityOrderByQuantityDesc(String city);
}