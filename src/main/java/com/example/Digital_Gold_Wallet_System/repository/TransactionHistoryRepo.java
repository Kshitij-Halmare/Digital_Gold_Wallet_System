package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionHistoryRepo extends JpaRepository<TransactionHistory, Integer> {
    List<TransactionHistory> findByBranchBranchId(Integer branchId);

    // Used by TC-22,23
    List<TransactionHistory> findByUserUserId(Integer userId);

    // Used by TC-24,25
    List<TransactionHistory> findByTransactionType(PaymentTransactionType type);

    // Used by TC-26,27
    List<TransactionHistory> findByTransactionStatus(TransactionStatus status);

    // Used by TC-28,29
    List<TransactionHistory> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // Used by TC-32,33
    List<TransactionHistory> findByBranchBranchIdAndTransactionType(
            Integer branchId, PaymentTransactionType type);

    // Used by TC-34,35
    List<TransactionHistory> findByBranchBranchIdAndTransactionStatus(
            Integer branchId, TransactionStatus status);

    // Used by TC-36,37
    List<TransactionHistory> findByBranchBranchIdAndCreatedAtBetween(
            Integer branchId, LocalDateTime from, LocalDateTime to);
}
