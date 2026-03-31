package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "physicalGoldTransaction",types = PhysicalGoldTransactions.class)
public interface VendorPhysicalGoldTransactionProjection {

    Integer getTransactionId();
    BigDecimal getQuantity();
    LocalDateTime getCreatedAt();

    UsersProjection getUser();

    AddressProjection getAddress();

}
