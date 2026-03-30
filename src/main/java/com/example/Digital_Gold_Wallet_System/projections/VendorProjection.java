package com.example.Digital_Gold_Wallet_System.projections;

import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "vendorDetails", types = Vendors.class)
public interface VendorProjection {
    String getVendorName();

    String getDescription();

    String getContactPersonName();

    String getContactEmail();

    String getContactPhone();

    String getWebsiteUrl();

    BigDecimal getTotalGoldQuantity();

    BigDecimal getCurrentGoldPrice();
}
