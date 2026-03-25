package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepo extends JpaRepository<Payments, Integer> {

}
