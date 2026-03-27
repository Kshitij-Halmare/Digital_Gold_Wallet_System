package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Payments;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "payments",
        collectionResourceRel = "payments"
)
public interface PaymentsRepo extends JpaRepository<Payments, Integer> {
    List<Payments> findByUser_UserId(Integer userId);
    List<Payments> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payments> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payments> findByPaymentTransactionType(PaymentTransactionType paymentTransactionType);
}
