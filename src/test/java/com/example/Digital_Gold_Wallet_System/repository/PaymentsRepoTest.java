package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Payments;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;

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
        assertNotNull(savedPayment.getPaymentId());
        assertEquals(new BigDecimal("5000.00"), savedPayment.getAmount());
    }

    @Test
    @DisplayName("Save Payment with negative amount")
    void testSavePaymentInvalidData() {

        Payments payment = new Payments();
        payment.setAmount(new BigDecimal(-89));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            paymentsRepository.save(payment);
            paymentsRepository.flush();
        });
    }

    @Test
    @DisplayName("Save Payment with null amount")
    void testSavePaymentNullData() {

        Payments payment = new Payments();
        payment.setAmount(null);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            paymentsRepository.save(payment);
            paymentsRepository.flush();
        });
    }

    @Test
    @DisplayName("Test Find by ID")
    void testFindByIdPayment() {

        Payments payment = new Payments();
        payment.setAmount(new BigDecimal("5000.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        Payments savedPayment = paymentsRepository.save(payment);

        Optional<Payments> foundPayment = paymentsRepository.findById(savedPayment.getPaymentId());
        assertTrue(foundPayment.isPresent());
    }

    @Test
    @DisplayName("Find Payment by invalid ID")
    void testFindPaymentByInvalidId() {

        Optional<Payments> payment =
                paymentsRepository.findById(9999);

        assertTrue(payment.isEmpty());
    }

}
