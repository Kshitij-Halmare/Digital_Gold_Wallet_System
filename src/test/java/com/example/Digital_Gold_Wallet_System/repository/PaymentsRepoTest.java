package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Payments;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.Digital_Gold_Wallet_System.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.enums.PaymentTransactionType;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PaymentsRepoTest {
    @Autowired
    private PaymentsRepo paymentsRepository;

    @Test
    @DisplayName("Test Save Payment")
    void testSavePayment() {

        Payments payment = new Payments();
        payment.setAmount(new BigDecimal("5000.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        Payments savedPayment = paymentsRepository.save(payment);

        assertNotNull(savedPayment);
        assertNotNull(savedPayment.getPayment_id());
        assertEquals(new BigDecimal("5000.00"), savedPayment.getAmount());
    }

}
