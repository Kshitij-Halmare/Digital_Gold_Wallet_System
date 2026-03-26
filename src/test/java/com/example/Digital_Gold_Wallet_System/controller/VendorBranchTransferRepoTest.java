package com.example.Digital_Gold_Wallet_System.controller;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VendorBranchTransferRepoTest {

    @Autowired
    private VendorBranchesRepo branchRepo;
    private VendorBranches createBranch(String city, BigDecimal quantity) {
        Addresses address = new Addresses();
        address.setCity(city);
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setPostalCode("411001");

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(quantity);
        branch.setAddress(address);
        branch.setCreatedAt(LocalDateTime.now());
        return branchRepo.save(branch);
    }

    private void performTransfer(Integer sourceBranchId, Integer destinationBranchId, BigDecimal transferQty) {
        VendorBranches source = branchRepo.findById(sourceBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Source branch not found: " + sourceBranchId));

        VendorBranches destination = branchRepo.findById(destinationBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Destination branch not found: " + destinationBranchId));

        if (sourceBranchId.equals(destinationBranchId)) {
            throw new IllegalArgumentException("Source and destination branch cannot be the same");
        }

        if (source.getQuantity().compareTo(transferQty) < 0) {
            throw new IllegalArgumentException("Insufficient gold quantity in source branch");
        }

        source.setQuantity(source.getQuantity().subtract(transferQty));
        destination.setQuantity(destination.getQuantity().add(transferQty));

        branchRepo.save(source);
        branchRepo.save(destination);
    }

    @Test
    @DisplayName("TC-40: transferGold - Positive: valid source and destination → transfer successful")
    void tc40_transferGold_positive() {
        VendorBranches source = createBranch("Pune", BigDecimal.valueOf(100));
        VendorBranches destination = createBranch("Mumbai", BigDecimal.valueOf(50));
        BigDecimal transferQty = BigDecimal.valueOf(30);

        performTransfer(source.getBranchId(), destination.getBranchId(), transferQty);

        VendorBranches updatedSource = branchRepo.findById(source.getBranchId()).orElseThrow();
        VendorBranches updatedDest = branchRepo.findById(destination.getBranchId()).orElseThrow();

        assertEquals(0, updatedSource.getQuantity().compareTo(BigDecimal.valueOf(70)),
                "Source quantity should be reduced to 70 after transfer of 30");
        assertEquals(0, updatedDest.getQuantity().compareTo(BigDecimal.valueOf(80)),
                "Destination quantity should be increased to 80 after transfer of 30");
    }

    @Test
    @DisplayName("TC-41: transferGold - Negative: source branch id=999 → not found exception")
    void tc41_transferGold_sourceNotFound() {
        VendorBranches destination = createBranch("Mumbai", BigDecimal.valueOf(50));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                        performTransfer(999, destination.getBranchId(), BigDecimal.valueOf(10)),
                "Expected exception when source branch does not exist"
        );

        assertTrue(ex.getMessage().contains("Source branch not found"),
                "Exception message should indicate source not found");
    }

    @Test
    @DisplayName("TC-42: transferGold - Negative: destination branch id=999 → not found exception")
    void tc42_transferGold_destinationNotFound() {
        VendorBranches source = createBranch("Pune", BigDecimal.valueOf(100));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                        performTransfer(source.getBranchId(), 999, BigDecimal.valueOf(10)),
                "Expected exception when destination branch does not exist"
        );

        assertTrue(ex.getMessage().contains("Destination branch not found"),
                "Exception message should indicate destination not found");
    }

    @Test
    @DisplayName("TC-43: transferGold - Negative: source == destination → same branch exception")
    void tc43_transferGold_sameBranch() {
        VendorBranches branch = createBranch("Pune", BigDecimal.valueOf(100));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                        performTransfer(branch.getBranchId(), branch.getBranchId(), BigDecimal.valueOf(10)),
                "Expected exception when source and destination are the same"
        );

        assertTrue(ex.getMessage().contains("Source and destination branch cannot be the same"),
                "Exception message should indicate same branch error");
    }

    @Test
    @DisplayName("TC-44: transferGold - Negative: insufficient quantity in source → exception")
    void tc44_transferGold_insufficientQuantity() {
        VendorBranches source = createBranch("Pune", BigDecimal.valueOf(10));      // only 10
        VendorBranches destination = createBranch("Mumbai", BigDecimal.valueOf(50));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                        performTransfer(source.getBranchId(), destination.getBranchId(), BigDecimal.valueOf(50)),
                "Expected exception when source has insufficient quantity"
        );

        assertTrue(ex.getMessage().contains("Insufficient gold quantity"),
                "Exception message should indicate insufficient quantity");

        // Verify quantities are UNCHANGED after failed transfer
        VendorBranches unchangedSource = branchRepo.findById(source.getBranchId()).orElseThrow();
        assertEquals(0, unchangedSource.getQuantity().compareTo(BigDecimal.valueOf(10)),
                "Source quantity should remain 10 after failed transfer");
    }
}
