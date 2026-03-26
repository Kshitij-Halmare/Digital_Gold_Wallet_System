package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource (path = "physicalgoldtransaction")
public interface PhysicalGoldTransactionsRepo extends JpaRepository<PhysicalGoldTransactions, Integer> {

    //List<PhysicalGoldTransactions> findByBranchId(Integer branchId);
    List<PhysicalGoldTransactions> findByQuantity(Integer quantity);

}
