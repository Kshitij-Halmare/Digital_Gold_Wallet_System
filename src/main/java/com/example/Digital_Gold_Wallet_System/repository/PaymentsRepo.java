package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Payments;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentsRepo extends JpaRepository<Payments, Integer> {
    List<Payments> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payments> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payments> findByPaymentTransactionType(PaymentTransactionType paymentTransactionType);
}
