package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource (path = "physicalgoldtransaction")
public interface PhysicalGoldTransactionsRepo extends JpaRepository<PhysicalGoldTransactions, Integer> {

    List<PhysicalGoldTransactions> findByBranch_BranchId(Integer branchId);
    List<PhysicalGoldTransactions> findByQuantity(Integer quantity);
    List<PhysicalGoldTransactions> findByDeliveryAddress_Street(String street);
    List<PhysicalGoldTransactions> findByDeliveryAddress_City(String city);
    List<PhysicalGoldTransactions> findByDeliveryAddress_State(String state);
    List<PhysicalGoldTransactions> findByDeliveryAddress_PostalCode(String postalCode);
    List<PhysicalGoldTransactions> findByDeliveryAddress_Country(String country);

    List<PhysicalGoldTransactions> findByDeliveryAddress_CityAndDeliveryAddress_State(String city, String state);

    List<PhysicalGoldTransactions> findByUser_Name(String name);
}
