package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "branches")
public interface VendorBranchesRepo extends JpaRepository<VendorBranches, Integer> {

    List<VendorBranches> findByAddressCity(String city);
    List<VendorBranches> findByAddressState(String state);
    List<VendorBranches> findByAddressCountry(String country);
    List<VendorBranches> findByAddressPostalCode(String postalCode);
    List<VendorBranches> findByQuantityBetween(BigDecimal min, BigDecimal max);
    List<VendorBranches> findByAddressCityAndAddressState(String city, String state);
    List<VendorBranches> findByAddressStateAndAddressCountry(String state, String country);    List<VendorBranches> findByAddressCityContainingIgnoreCase(String city);
    List<VendorBranches> findByAddressStateContainingIgnoreCase(String state);
    List<VendorBranches> findByAddressCityOrderByQuantityAsc(String city);
    List<VendorBranches> findByAddressCityOrderByQuantityDesc(String city);
}