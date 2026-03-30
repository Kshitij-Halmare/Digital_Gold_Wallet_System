package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.Projection.VendorBranchProjection;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(
        path = "branches",
        collectionResourceRel = "branches",
        excerptProjection = VendorBranchProjection.class
)public interface VendorBranchesRepo extends JpaRepository<VendorBranches, Integer> {

    List<VendorBranchProjection> findAllBy();
    List<VendorBranches> findByAddressCity(String city);
    List<VendorBranches> findByAddressState(String state);
    List<VendorBranches> findByAddressCountry(String country);
    List<VendorBranches> findByAddressPostalCode(String postalCode);
    List<VendorBranches> findByQuantityBetween(BigDecimal min, BigDecimal max);
    List<VendorBranches> findByAddressCityAndAddressState(String city, String state);
    List<VendorBranches> findByVendorsVendorId(Integer vendorId);
    List<VendorBranches> findByVendorsVendorIdAndAddressCity(Integer vendorId, String city);
}