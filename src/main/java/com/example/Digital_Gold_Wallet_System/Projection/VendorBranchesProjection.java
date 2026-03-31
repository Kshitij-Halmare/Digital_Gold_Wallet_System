package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "branchDetails", types = VendorBranches.class)
public interface VendorBranchesProjection {
    Long getBranchId();

    BigDecimal getQuantity();

    Addresses getAddress();
}
