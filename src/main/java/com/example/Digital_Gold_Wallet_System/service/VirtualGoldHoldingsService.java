package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.*;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;
import com.example.Digital_Gold_Wallet_System.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VirtualGoldHoldingsService {
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private VendorBranchesRepo vendorBranchesRepo;

    @Autowired
    private VirtualGoldHoldingsRepo holdingRepo;

    @Autowired
    private PhysicalGoldTransactionsRepo phytxnRepo;
    @Autowired
    private VendorsRepo vendorsRepo;

    @Autowired
    private TransactionHistoryRepo txnRepo;

    @Transactional
    public void convertToPhysical(Integer holdingId) {
        //get  holding
        VirtualGoldHoldings holding = holdingRepo.findById(holdingId).orElseThrow(() -> new RuntimeException("Holding not found"));

        //check if already converted
        if (holding.isConverted()) {
            throw new RuntimeException("Already converted");
        }

        //check address
        if (holding.getUser() == null || holding.getUser().getAddress() == null) {
            throw new RuntimeException("Delivery address required");
        }

        //create physical gold transaction
        PhysicalGoldTransactions phytxn = new PhysicalGoldTransactions();
        phytxn.setUser(holding.getUser());
        phytxn.setBranch(holding.getBranch());
        phytxn.setQuantity(holding.getQuantity());
        phytxn.setCreatedAt(LocalDateTime.now());
        phytxn.setDeliveryAddress(holding.getUser().getAddress());

        phytxnRepo.save(phytxn);

        //calculate total amount based on current price
        VendorBranches branch = holding.getBranch();
        Vendors vendor = branch.getVendors();
        BigDecimal currentPrice = vendor.getCurrentGoldPrice();
        BigDecimal amount = holding.getQuantity().multiply(currentPrice);

        //create transaction of type convert
        TransactionHistory txn = new TransactionHistory();
        txn.setTransactionType(TransactionType.CONVERT_TO_PHYSICAL);
        txn.setTransactionStatus(TransactionStatus.SUCCESS);
        txn.setQuantity(holding.getQuantity());
        txn.setAmount(amount);
        txn.setUser(holding.getUser());
        txn.setBranch(holding.getBranch());
        txn.setCreatedAt(LocalDateTime.now());

        txnRepo.save(txn);


        holding.setConverted(true);
        holdingRepo.save(holding);
    }
}
