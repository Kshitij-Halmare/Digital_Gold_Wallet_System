package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.repository.TransactionHistoryRepo;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TransactionControllerRepoTest {

    @Autowired
    private TransactionHistoryRepo transactionRepo;

    @Autowired
    private VendorBranchesRepo branchRepo;

    @Autowired
    private UsersRepo usersRepo;
    private VendorBranches createBranch() {
        Addresses address = new Addresses();
        address.setCity("Pune");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setPostalCode("411001");

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(BigDecimal.valueOf(100));
        branch.setAddress(address);
        branch.setCreatedAt(LocalDateTime.now());

        return branchRepo.save(branch);
    }

    private Users createUser() {
        Users user = new Users();
        user.setName("Test User");
        // UUID ensures no two tests ever collide on the unique email constraint
        user.setEmail("test_" + UUID.randomUUID() + "@example.com");
        user.setCreatedAt(LocalDateTime.now());

        return usersRepo.save(user);
    }

    private TransactionHistory createTransaction(
            VendorBranches branch,
            Users user,
            PaymentTransactionType type,
            TransactionStatus status,
            LocalDateTime createdAt
    ) {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionType(type);
        tx.setTransactionStatus(status);
        tx.setQuantity(BigDecimal.valueOf(5));
        tx.setAmount(BigDecimal.valueOf(30000));
        tx.setBranch(branch);
        tx.setUser(user);
        tx.setCreatedAt(createdAt);

        return transactionRepo.save(tx);
    }
    @Test
    @DisplayName("TC-20: getTransactionsByBranchId - Positive")
    void tc20_getTransactionsByBranchId_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByBranchBranchId(branch.getBranchId());

        assertFalse(result.isEmpty());
        result.forEach(tx -> assertEquals(branch.getBranchId(), tx.getBranch().getBranchId()));
    }

    @Test
    @DisplayName("TC-21: getTransactionsByBranchId - Negative")
    void tc21_getTransactionsByBranchId_notFound() {
        List<TransactionHistory> result = transactionRepo.findByBranchBranchId(999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-22: getTransactionsByUserId - Positive")
    void tc22_getTransactionsByUserId_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByUserUserId(user.getUserId());

        assertFalse(result.isEmpty());
        result.forEach(tx -> assertEquals(user.getUserId(), tx.getUser().getUserId()));
    }

    @Test
    @DisplayName("TC-23: getTransactionsByUserId - Negative")
    void tc23_getTransactionsByUserId_notFound() {
        List<TransactionHistory> result = transactionRepo.findByUserUserId(999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-24: getTransactionsByType - Positive")
    void tc24_getTransactionsByType_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByTransactionType(PaymentTransactionType.CREDITED);

        assertFalse(result.isEmpty());
        result.forEach(tx -> assertEquals(PaymentTransactionType.CREDITED, tx.getTransactionType()));
    }

    @Test
    @DisplayName("TC-25: getTransactionsByType - Negative")
    void tc25_getTransactionsByType_noMatch() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByTransactionType(PaymentTransactionType.DEBITED);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-26: getTransactionsByStatus - Positive")
    void tc26_getTransactionsByStatus_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByTransactionStatus(TransactionStatus.SUCCESS);

        assertFalse(result.isEmpty());
        result.forEach(tx -> assertEquals(TransactionStatus.SUCCESS, tx.getTransactionStatus()));
    }

    @Test
    @DisplayName("TC-27: getTransactionsByStatus - Negative")
    void tc27_getTransactionsByStatus_noMatch() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo.findByTransactionStatus(TransactionStatus.FAILED);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-28: getTransactionsByDateRange - Positive")
    void tc28_getTransactionsByDateRange_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        LocalDateTime txTime = LocalDateTime.of(2024, 6, 15, 10, 0);
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, txTime);

        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);

        List<TransactionHistory> result = transactionRepo.findByCreatedAtBetween(from, to);

        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("TC-29: getTransactionsByDateRange - Negative")
    void tc29_getTransactionsByDateRange_invalidRange() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        LocalDateTime txTime = LocalDateTime.of(2024, 6, 15, 10, 0);
        createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, txTime);

        LocalDateTime from = LocalDateTime.of(2024, 12, 31, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 1, 0, 0);

        List<TransactionHistory> result = transactionRepo.findByCreatedAtBetween(from, to);

        assertTrue(result.isEmpty());
    }

    // TC-30
    @Test
    @DisplayName("TC-30: getTransactionById - Positive")
    void tc30_getTransactionById_positive() {
        VendorBranches branch = createBranch();
        Users user = createUser();
        TransactionHistory saved = createTransaction(branch, user, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        Optional<TransactionHistory> result = transactionRepo.findById(saved.getTransactionId());

        assertTrue(result.isPresent());
        assertEquals(saved.getTransactionId(), result.get().getTransactionId());
    }

    // TC-31
    @Test
    @DisplayName("TC-31: getTransactionById - Negative")
    void tc31_getTransactionById_notFound() {
        Optional<TransactionHistory> result = transactionRepo.findById(999);

        assertFalse(result.isPresent());
    }
}