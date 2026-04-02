package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionHistoryProjection {

    TransactionType getTransactionType();

    TransactionStatus getTransactionStatus();

    BigDecimal getQuantity();

    BigDecimal getAmount();

    LocalDateTime getCreatedAt();

    // Optional: nested projection for user
    UserInfo getUser();

    interface UserInfo {
        Integer getUserId();
        String getName(); // adjust based on your Users entity
    }

    // Optional: nested projection for branch
    BranchInfo getBranch();

    interface BranchInfo {
        Integer getBranchId();
//        String getBranchName(); // adjust based on your VendorBranches entity
    }
}