//package com.example.Digital_Gold_Wallet_System.repository;
//
//import com.example.Digital_Gold_Wallet_System.entity.Vendors;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import java.util.List;
//
//@RepositoryRestResource(
//        path = "vendors",
//        collectionResourceRel = "vendors"
//)
//public interface VendorsRepo extends JpaRepository<Vendors, Integer> {
//    List<Vendors> findByVendorName(String vendorName);
//}

package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(
        path = "vendors",
        collectionResourceRel = "vendors"
)
public interface VendorsRepo extends JpaRepository<Vendors, Integer> {
    List<Vendors> findByVendorName(String vendorName);
}