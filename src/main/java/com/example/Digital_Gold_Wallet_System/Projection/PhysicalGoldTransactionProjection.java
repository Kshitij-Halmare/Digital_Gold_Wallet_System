package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;

import java.math.BigDecimal;

public interface PhysicalGoldTransactionProjection {
    Integer getTransactionId();

    TransactionType getTransactionType();

    BigDecimal getAmount();

    BigDecimal getQuantity();

    VendorBranchProjectionUserPhysical getBranch();

//    PaymentProjection getPayments();
}
