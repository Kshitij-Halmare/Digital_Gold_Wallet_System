package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.Projection.VendorVirtualGoldHoldingsProjection;
import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
@CrossOrigin(origins = "*")
@RepositoryRestResource(
        path = "virtual_gold_holdings",
        collectionResourceRel = "holdings",
        excerptProjection = VendorVirtualGoldHoldingsProjection.class
)
public interface VirtualGoldHoldingsRepo extends JpaRepository<VirtualGoldHoldings, Integer> {

    @RestResource(path = "findByVendor")
    List<VirtualGoldHoldings> findByBranch_Vendors_VendorId(@Param("vendorId") Integer vendorId);
    List<VirtualGoldHoldings> findByBranch_BranchId(Integer branchId);
    List<VirtualGoldHoldings> findByQuantity(Integer quantity);
    List<VirtualGoldHoldings> findByUser_UserId(Integer userId);
    List<VirtualGoldHoldings> findByUser_Name(String name);
    List<VirtualGoldHoldings> findByUser_UserIdAndBranch_BranchId(Integer userId, Integer branchId);
}
