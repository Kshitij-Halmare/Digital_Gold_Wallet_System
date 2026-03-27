package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class TransactionHistoryRepoTest {
    @Autowired
    private TransactionHistoryRepo repository;

    private TransactionHistory createTransaction() {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionType(TransactionType.BUY);
        tx.setTransactionStatus(TransactionStatus.SUCCESS);
        tx.setQuantity(new BigDecimal("10.50"));
        tx.setAmount(new BigDecimal("55000.75"));
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }

    @Test
    @DisplayName("Save Transaction - Positive Case")
    void testSaveTransaction() {
        TransactionHistory tx = createTransaction();

        TransactionHistory saved = repository.save(tx);

        assertNotNull(saved.getTransactionId());
        assertEquals(TransactionType.BUY, saved.getTransactionType());
    }

    @Test
    @DisplayName("Find By ID - Positive Case")
    void testFindById() {
        TransactionHistory saved = repository.save(createTransaction());

        Optional<TransactionHistory> result = repository.findById(saved.getTransactionId());

        assertTrue(result.isPresent());
        assertEquals(saved.getTransactionId(), result.get().getTransactionId());
    }

    @Test
    @DisplayName("Find By ID - Negative Case")
    void testFindByIdNotFound() {
        Optional<TransactionHistory> result = repository.findById(999);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Find All Transactions")
    void testFindAll() {
        repository.save(createTransaction());
        repository.save(createTransaction());

        List<TransactionHistory> list = repository.findAll();

        assertFalse(list.isEmpty());
        assertTrue(list.size() >= 2);
    }

}
