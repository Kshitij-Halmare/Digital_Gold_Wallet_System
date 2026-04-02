package com.example.Digital_Gold_Wallet_System.controllers;

import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.service.VendorBranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/branches")
@CrossOrigin(origins = "*")
public class VendorBranchController {

    private final VendorBranchService branchService;

    public VendorBranchController(VendorBranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferGold(
            @RequestParam Integer fromBranchId,
            @RequestParam Integer toBranchId,
            @RequestParam BigDecimal quantity
    ) {
        branchService.transferGold(fromBranchId, toBranchId, quantity);
        return ResponseEntity.ok("Transfer successful");
    }

    @PutMapping("/updateQuantity")
    public String updateQuantity(@RequestBody VendorBranches vendorBranches){
        return branchService.addQuantityToVendor(vendorBranches);
    }
}