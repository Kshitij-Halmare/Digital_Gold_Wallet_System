package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import com.example.Digital_Gold_Wallet_System.entity.enums.HoldingStatus;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "virtualGoldHolding",types = VirtualGoldHoldings.class)
public interface VendorVirtualGoldHoldingsProjection {

    Integer getHoldingId();
    BigDecimal getQuantity();
    LocalDateTime getCreatedAt();
    HoldingStatus getHoldingStatus();
    UserListProjection getUser();
}
