package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.*;
import com.example.Digital_Gold_Wallet_System.exception.InsufficientGoldException;
import com.example.Digital_Gold_Wallet_System.repository.PhysicalGoldTransactionsRepo;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import com.example.Digital_Gold_Wallet_System.repository.VirtualGoldHoldingsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    private PhysicalGoldTransactionsRepo txnRepo;

    @Transactional
    public void convertToPhysical(Integer holdingId) {

        VirtualGoldHoldings holding = holdingRepo.findById(holdingId).orElseThrow(() -> new RuntimeException("Holding not found"));

        if (holding.isConverted()) {
            throw new RuntimeException("Already converted");
        }

        if (holding.getUser() == null || holding.getUser().getAddress() == null) {
            throw new RuntimeException("Delivery address required");
        }

        PhysicalGoldTransactions txn = new PhysicalGoldTransactions();
        txn.setUser(holding.getUser());
        txn.setBranch(holding.getBranch());
        txn.setQuantity(holding.getQuantity());
        txn.setCreatedAt(LocalDateTime.now());
        txn.setDeliveryAddress(holding.getUser().getAddress());

        txnRepo.save(txn);

//        VendorBranches vendorBranch = holdinf;

        holding.setConverted(true);
        holdingRepo.save(holding);
    }

    @Transactional
    public void buyVirtualGoldHoldings(Integer userId, BigDecimal qty, Integer branchId) {

        Users user = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        VendorBranches branch = vendorBranchesRepo.findById(branchId).orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Vendors vendor = branch.getVendors();

        if(qty.compareTo(branch.getQuantity()) == 1) {
            throw new InsufficientGoldException("Not enough gold in the given branch...");
        }

        VirtualGoldHoldings holding = new VirtualGoldHoldings();
        holding.setQuantity(qty);
        holding.setCreatedAt(LocalDateTime.now());
        holding.setUser(user);
        holding.setBranch(branch);
        holding.setConverted(false);

        holdingRepo.save(holding);

    }
}
