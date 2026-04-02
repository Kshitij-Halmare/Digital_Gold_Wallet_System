package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PhysicalGoldTransactionProjection {
    Integer getTransactionId();

    LocalDateTime getCreatedAt();

    TransactionType getTransactionType();
    TransactionStatus getTransactionStatus();

    BigDecimal getAmount();

    BigDecimal getQuantity();

    VendorBranchProjectionUserPhysical getBranch();

//    PaymentProjection getPayments();
}
