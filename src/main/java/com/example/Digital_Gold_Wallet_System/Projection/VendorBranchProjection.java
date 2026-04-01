package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "vendorBranchView", types = VendorBranches.class)
public interface VendorBranchProjection {

    int getBranchId();
    BigDecimal getQuantity();
    LocalDateTime getCreatedAt();

    AddressView getAddress();
    VendorView getVendors();

    interface AddressView {
        Integer getAddressId();
        String getStreet();
        String getCity();
        String getState();
        String getPostalCode();
        String getCountry();
    }

    interface VendorView {
        Integer getVendorId();
        String getVendorName();
    }
}