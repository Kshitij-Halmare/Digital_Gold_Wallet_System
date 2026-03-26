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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentsRepoTest {
    @Autowired
    private PaymentsRepo paymentsRepository;

    private Payments createPayment() {
        Payments payment = new Payments();
        payment.setAmount(new BigDecimal("5000.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    @Test
    @DisplayName("Test Save Payment")
    void testSavePayment() {

        Payments payment = createPayment();

        Payments savedPayment = paymentsRepository.save(payment);

        assertNotNull(savedPayment);
        assertNotNull(savedPayment.getPayment_id());
        assertEquals(new BigDecimal("5000.00"), savedPayment.getAmount());
    }

    @Test
    @DisplayName("Test Find All Payments")
    void testFindAllPayments() {

        paymentsRepository.save(createPayment());
        paymentsRepository.save(createPayment());

        List<Payments> payments = paymentsRepository.findAll();

        assertNotNull(payments);
        assertTrue(payments.size() >= 2);
    }

    @Test
    @DisplayName("Test Find Payment By ID")
    void testFindPaymentById() {

        Payments savedPayment = paymentsRepository.save(createPayment());

        Optional<Payments> payment = paymentsRepository.findById(savedPayment.getPayment_id());

        assertTrue(payment.isPresent());
        assertEquals(savedPayment.getPayment_id(), payment.get().getPayment_id());
    }

    @Test
    @DisplayName("Test Delete Payment")
    void testDeletePayment() {

        Payments savedPayment = paymentsRepository.save(createPayment());

        paymentsRepository.deleteById(savedPayment.getPayment_id());

        Optional<Payments> payment = paymentsRepository.findById(savedPayment.getPayment_id());

        assertFalse(payment.isPresent());
    }

    @Test
    @DisplayName("Test Find By Payment Status")
    void testFindByPaymentStatus() {

        Payments payment = createPayment();
        paymentsRepository.save(payment);

        List<Payments> payments = paymentsRepository.findByPaymentStatus(PaymentStatus.SUCCESS);

        assertNotNull(payments);
        assertFalse(payments.isEmpty());
        assertEquals(PaymentStatus.SUCCESS, payments.get(0).getPaymentStatus());
    }

    @Test
    @DisplayName("Test Find By Payment Method")
    void testFindByPaymentMethod() {

        Payments payment = createPayment();
        paymentsRepository.save(payment);

        List<Payments> payments = paymentsRepository.findByPaymentMethod(PaymentMethod.UPI);

        assertNotNull(payments);
        assertFalse(payments.isEmpty());
        assertEquals(PaymentMethod.UPI, payments.get(0).getPaymentMethod());
    }

    @Test
    @DisplayName("Test Find By Transaction Type")
    void testFindByTransactionType() {

        Payments payment = createPayment();
        paymentsRepository.save(payment);

        List<Payments> payments = paymentsRepository.findByPaymentTransactionType(PaymentTransactionType.CREDITED);

        assertNotNull(payments);
        assertFalse(payments.isEmpty());
        assertEquals(PaymentTransactionType.CREDITED, payments.get(0).getPaymentTransactionType());
    }

}
