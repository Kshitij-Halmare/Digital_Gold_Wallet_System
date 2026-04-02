package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.Projection.VendorBranchProjection;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.util.List;
@CrossOrigin(origins = "*")
@RepositoryRestResource(
        path = "vendorBranches",
        collectionResourceRel = "vendorBranches",
        excerptProjection = VendorBranchProjection.class
)
@CrossOrigin(origins = "http://localhost:9090")
public interface VendorBranchesRepo extends JpaRepository<VendorBranches, Integer> {

    List<VendorBranches> findAll();
    List<VendorBranches> findByAddressCity(String city);
    List<VendorBranches> findByAddressState(String state);
    List<VendorBranches> findByAddressCountry(String country);
    List<VendorBranches> findByAddressPostalCode(String postalCode);
    List<VendorBranches> findByQuantityBetween(BigDecimal min, BigDecimal max);
    List<VendorBranches> findByAddressCityAndAddressState(String city, String state);
    List<VendorBranches> findByVendorsVendorId(Integer vendorId);
    List<VendorBranches> findByVendorsVendorIdAndAddressCity(Integer vendorId, String city);

    List<VendorBranches> findByAddressCityContainingIgnoreCase(String pun);

    List<VendorBranches> findByAddressCityOrderByQuantityAsc(String pune);

    List<VendorBranches> findByAddressCityOrderByQuantityDesc(String pune);
}