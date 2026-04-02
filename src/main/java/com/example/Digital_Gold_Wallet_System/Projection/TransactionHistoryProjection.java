package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "transactionHistoryView", types = TransactionHistory.class)
public interface TransactionHistoryProjection {

    Integer getTransactionId();

    TransactionType getTransactionType();

    TransactionStatus getTransactionStatus();

    BigDecimal getQuantity();

    BigDecimal getAmount();

    LocalDateTime getCreatedAt();


    UserInfo getUser();

    interface UserInfo {
        Integer getUserId();
        String getName(); // must match Users entity field
    }


    BranchInfo getBranch();

    interface BranchInfo {
        Long getBranchId();
    }
}
