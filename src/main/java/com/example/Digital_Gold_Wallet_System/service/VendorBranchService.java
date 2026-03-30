package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.exception.BranchNotFoundException;
import com.example.Digital_Gold_Wallet_System.exception.InsufficientGoldException;
import com.example.Digital_Gold_Wallet_System.exception.VendorMismatchException;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VendorBranchService {

    private final VendorBranchesRepo branchRepo;

    public VendorBranchService(VendorBranchesRepo branchRepo) {
        this.branchRepo = branchRepo;
    }

    @Transactional
    public void transferGold(Integer fromBranchId, Integer toBranchId, BigDecimal quantity) {
        VendorBranches from = branchRepo.findById(fromBranchId)
                .orElseThrow(() -> new BranchNotFoundException("Source branch not found: " + fromBranchId));
        VendorBranches to = branchRepo.findById(toBranchId)
                .orElseThrow(() -> new BranchNotFoundException("Destination branch not found: " + toBranchId));

        if (!from.getVendors().getVendorId().equals(to.getVendors().getVendorId())) {
            throw new VendorMismatchException("Branches belong to different vendors!");
        }

        if (from.getQuantity().compareTo(quantity) < 0) {
            throw new InsufficientGoldException("Not enough gold in source branch. Available: "
                    + from.getQuantity() + ", Required: " + quantity);
        }

        from.setQuantity(from.getQuantity().subtract(quantity));
        to.setQuantity(to.getQuantity().add(quantity));

        branchRepo.save(from);
        branchRepo.save(to);
    }
}