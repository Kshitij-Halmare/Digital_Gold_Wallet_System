package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "vendorBranchView", types = VendorBranches.class)
public interface VendorBranchProjection {
    BigDecimal getQuantity();
    LocalDateTime getCreatedAt();
    Addresses getAddress();
}