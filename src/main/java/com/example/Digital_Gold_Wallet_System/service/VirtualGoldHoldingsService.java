package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import com.example.Digital_Gold_Wallet_System.repository.PhysicalGoldTransactionsRepo;
import com.example.Digital_Gold_Wallet_System.repository.VirtualGoldHoldingsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VirtualGoldHoldingsService {
    @Autowired
    private VirtualGoldHoldingsRepo holdingRepo;

    @Autowired
    private PhysicalGoldTransactionsRepo txnRepo;

    @Transactional
    public void convertToPhysical(Integer holdingId) {

        // ✅ 1. Fetch holding
        VirtualGoldHoldings holding = holdingRepo.findById(holdingId)
                .orElseThrow(() -> new RuntimeException("Holding not found"));

        // ❌ 2. Already converted
        if (holding.isConverted()) {
            throw new RuntimeException("Already converted");
        }

        // ❌ 3. Delivery address validation
        if (holding.getUser() == null || holding.getUser().getAddress() == null) {
            throw new RuntimeException("Delivery address required");
        }

        // ✅ 4. Create physical transaction
        PhysicalGoldTransactions txn = new PhysicalGoldTransactions();
        txn.setUser(holding.getUser());
        txn.setBranch(holding.getBranch());
        txn.setQuantity(holding.getQuantity());
        txn.setCreatedAt(LocalDateTime.now());
        txn.setDeliveryAddress(holding.getUser().getAddress());

        txnRepo.save(txn);

        // ✅ 5. Update holding
        holding.setConverted(true);
        holdingRepo.save(holding);
    }
}
