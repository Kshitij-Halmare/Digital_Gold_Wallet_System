package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(
        path = "virtual_gold_holdings",
        collectionResourceRel = "holdings"
)
@CrossOrigin(origins = "*")
public interface VirtualGoldHoldingsRepo extends JpaRepository<VirtualGoldHoldings, Integer> {

    List<VirtualGoldHoldings> findByBranch_BranchId(Integer branchId);
    List<VirtualGoldHoldings> findByQuantity(Integer quantity);
    List<VirtualGoldHoldings> findByUser_UserId(Integer userId);
    List<VirtualGoldHoldings> findByUser_UserIdAndBranch_BranchId(Integer userId, Integer branchId);
}
