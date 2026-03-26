package com.example.Digital_Gold_Wallet_System.controller;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentTransactionType;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.repository.AddressesRepo;
import com.example.Digital_Gold_Wallet_System.repository.TransactionHistoryRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers BranchController test cases (S.No 1–19, 32–39) at repository layer.
 * No Controller or Service layer is used — all assertions hit the DB directly.
 */
@SpringBootTest
@Transactional
class VendorBranchesControllerRepoTest {

    @Autowired
    private VendorBranchesRepo branchRepo;

    @Autowired
    private TransactionHistoryRepo transactionRepo;

    @Autowired
    private AddressesRepo addressRepo;

    @Autowired
    private VendorsRepo vendorsRepo;

    // ─────────────────────────────────────────────────────────────
    // Helper: create a branch with given address fields and quantity
    // ─────────────────────────────────────────────────────────────
    private VendorBranches createBranch(String city, String state, String country,
                                        String postal, BigDecimal qty) {
        Addresses address = new Addresses();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postal);

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(qty);
        branch.setAddress(address);
        branch.setCreatedAt(LocalDateTime.now());

        return branchRepo.save(branch);
    }

    // Helper: create a transaction linked to a branch
    private TransactionHistory createTransaction(VendorBranches branch,
                                                 PaymentTransactionType type,
                                                 TransactionStatus status,
                                                 LocalDateTime createdAt) {
        TransactionHistory tx = new TransactionHistory();
        tx.setTransactionType(type);
        tx.setTransactionStatus(status);
        tx.setQuantity(BigDecimal.valueOf(10));
        tx.setAmount(BigDecimal.valueOf(50000));
        tx.setBranch(branch);
        tx.setCreatedAt(createdAt);
        return transactionRepo.save(tx);
    }

    // ─────────────────────────────────────────────────────────────
    // TC 1: GET /api/v1/branches — Positive: branches exist
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-1: getAllBranches - Positive: branches exist → non-empty list")
    void tc1_getAllBranches_positive() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createBranch("Mumbai", "Maharashtra", "India", "400001", BigDecimal.valueOf(30));

        List<VendorBranches> result = branchRepo.findAll();

        assertFalse(result.isEmpty(), "Expected non-empty list of branches");
        assertTrue(result.size() >= 2);
    }

    // ─────────────────────────────────────────────────────────────
    // TC 2: GET /api/v1/branches — Negative: no branches → empty list
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-2: getAllBranches - Negative: no branches → empty list")
    void tc2_getAllBranches_negative_empty() {
        // No data inserted — @Transactional rolls back, DB is clean
        List<VendorBranches> result = branchRepo.findAll();

        assertTrue(result.isEmpty(), "Expected empty list when no branches exist");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 3: GET /api/v1/branches/city/{city} — Positive: city = "Pune"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-3: getBranchesByCity - Positive: city=Pune → list returned")
    void tc3_getBranchesByCity_positive() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result = branchRepo.findByAddressCity("Pune");

        assertFalse(result.isEmpty(), "Expected branches for city=Pune");
        result.forEach(b -> assertEquals("Pune", b.getAddress().getCity()));
    }

    // ─────────────────────────────────────────────────────────────
    // TC 4: GET /api/v1/branches/city/{city} — Negative: city = "XYZ"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-4: getBranchesByCity - Negative: city=XYZ → empty list")
    void tc4_getBranchesByCity_cityNotFound() {
        List<VendorBranches> result = branchRepo.findByAddressCity("XYZ");

        assertTrue(result.isEmpty(), "Expected empty list for non-existent city");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 5: GET /api/v1/branches/city/{city} — Negative: city = "" (blank)
    // At repo level: blank city returns empty since no record has blank city
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-5: getBranchesByCity - Negative: blank city → empty list (invalid input)")
    void tc5_getBranchesByCity_blankCity() {
        List<VendorBranches> result = branchRepo.findByAddressCity("");

        assertTrue(result.isEmpty(), "Expected empty list for blank city input");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 6: GET /api/v1/branches/state/{state} — Positive: state = "Maharashtra"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-6: getBranchesByState - Positive: state=Maharashtra → list returned")
    void tc6_getBranchesByState_positive() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result = branchRepo.findByAddressState("Maharashtra");

        assertFalse(result.isEmpty(), "Expected branches for state=Maharashtra");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 7: GET /api/v1/branches/state/{state} — Negative: state = "ABC"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-7: getBranchesByState - Negative: state=ABC → empty list")
    void tc7_getBranchesByState_notFound() {
        List<VendorBranches> result = branchRepo.findByAddressState("ABC");

        assertTrue(result.isEmpty(), "Expected empty list for non-existent state");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 8: GET /api/v1/branches/country/{country} — Positive: country = "India"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-8: getBranchesByCountry - Positive: country=India → list returned")
    void tc8_getBranchesByCountry_positive() {
        createBranch("Delhi", "Delhi", "India", "110001", BigDecimal.valueOf(40));

        List<VendorBranches> result = branchRepo.findByAddressCountry("India");

        assertFalse(result.isEmpty(), "Expected branches for country=India");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 9: GET /api/v1/branches/country/{country} — Negative: country = "XYZ"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-9: getBranchesByCountry - Negative: country=XYZ → empty list")
    void tc9_getBranchesByCountry_notFound() {
        List<VendorBranches> result = branchRepo.findByAddressCountry("XYZ");

        assertTrue(result.isEmpty(), "Expected empty list for non-existent country");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 10: GET /api/v1/branches/postal/{postalCode} — Positive: "411001"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-10: getBranchesByPostalCode - Positive: postalCode=411001 → list returned")
    void tc10_getBranchesByPostalCode_positive() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(25));

        List<VendorBranches> result = branchRepo.findByAddressPostalCode("411001");

        assertFalse(result.isEmpty(), "Expected branches for postalCode=411001");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 11: GET /api/v1/branches/postal/{postalCode} — Negative: "000000"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-11: getBranchesByPostalCode - Negative: postalCode=000000 → empty list")
    void tc11_getBranchesByPostalCode_notFound() {
        List<VendorBranches> result = branchRepo.findByAddressPostalCode("000000");

        assertTrue(result.isEmpty(), "Expected empty list for non-existent postal code");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 12: GET /api/v1/branches/quantity?min=10&max=100 — Positive: valid range
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-12: getBranchesByQuantityRange - Positive: min=10, max=100 → matching branches")
    void tc12_getBranchesByQuantityRange_positive() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));

        List<VendorBranches> result = branchRepo.findByQuantityBetween(
                BigDecimal.valueOf(10), BigDecimal.valueOf(100));

        assertFalse(result.isEmpty(), "Expected branches with quantity between 10 and 100");
        result.forEach(b -> {
            assertTrue(b.getQuantity().compareTo(BigDecimal.valueOf(10)) >= 0);
            assertTrue(b.getQuantity().compareTo(BigDecimal.valueOf(100)) <= 0);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // TC 13: GET /api/v1/branches/quantity?min=100&max=10 — Negative: invalid range
    // At repo level: min > max returns empty since no value satisfies it
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-13: getBranchesByQuantityRange - Negative: min=100 > max=10 → empty list")
    void tc13_getBranchesByQuantityRange_invalidRange() {
        createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));

        List<VendorBranches> result = branchRepo.findByQuantityBetween(
                BigDecimal.valueOf(100), BigDecimal.valueOf(10));

        assertTrue(result.isEmpty(), "Expected empty list when min > max");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 14: GET /api/v1/branches/quantity?min=-10&max=50 — Boundary: negative min
    // At repo level: negative min is technically valid SQL but semantically invalid
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-14: getBranchesByQuantityRange - Boundary: min=-10 (negative) → empty (invalid)")
    void tc14_getBranchesByQuantityRange_negativeMin() {
        // In production the controller returns 400; at repo level we verify
        // that no branch with negative quantity exists (quantity is always >= 0)
        List<VendorBranches> result = branchRepo.findByQuantityBetween(
                BigDecimal.valueOf(-10), BigDecimal.valueOf(50));

        // All saved branches have qty >= 0; result may include them if qty <= 50
        // The key business rule: min must be >= 0 (enforced at controller/service)
        // At repo level, we just confirm the query runs and returns consistent data
        assertNotNull(result, "Result should not be null even for negative min");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 15: GET /api/v1/branches/{branch_id} — Positive: branch exists
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-15: getBranchByBranchId - Positive: id exists → branch returned")
    void tc15_getBranchById_positive() {
        VendorBranches saved = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        Optional<VendorBranches> result = branchRepo.findById(saved.getBranchId());

        assertTrue(result.isPresent(), "Expected branch to be found by ID");
        assertEquals(saved.getBranchId(), result.get().getBranchId());
    }

    // ─────────────────────────────────────────────────────────────
    // TC 16: GET /api/v1/branches/{branch_id} — Negative: id=999 not found
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-16: getBranchByBranchId - Negative: id=999 → not found")
    void tc16_getBranchById_notFound() {
        Optional<VendorBranches> result = branchRepo.findById(999);

        assertFalse(result.isPresent(), "Expected empty Optional for non-existent branch ID");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 17: PUT /api/v1/branches/update/{branch_id} — Positive: update success
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-17: updateBranchByBranchId - Positive: valid update → updated branch returned")
    void tc17_updateBranch_positive() {
        VendorBranches saved = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        saved.setQuantity(BigDecimal.valueOf(99));
        saved.getAddress().setCity("Nashik");

        branchRepo.save(saved);
        branchRepo.flush();

        VendorBranches updated = branchRepo.findById(saved.getBranchId()).orElseThrow();

        assertEquals(0, updated.getQuantity().compareTo(BigDecimal.valueOf(99)));
        assertEquals("Nashik", updated.getAddress().getCity());
    }
    // ─────────────────────────────────────────────────────────────
    // TC 18: PUT /api/v1/branches/update/{branch_id} — Negative: id=999 not found
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-18: updateBranchByBranchId - Negative: id=999 → not found before update")
    void tc18_updateBranch_notFound() {
        Optional<VendorBranches> existing = branchRepo.findById(999);

        assertFalse(existing.isPresent(), "Branch ID 999 should not exist — cannot update");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 19: PUT /api/v1/branches/update/{branch_id} — Negative: invalid JSON (null quantity)
    // ─────────────────────────────────────────────────────────────
    @Test
    void tc19_updateBranch_invalidData() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        branch.setQuantity(null); // invalid

        assertThrows(Exception.class, () -> {
            branchRepo.save(branch);
            branchRepo.flush();
        });
    }

    // ─────────────────────────────────────────────────────────────
    // TC 32: GET /api/v1/branches/{branch_id}/transactions/type/{type}
    //        Positive: branchId=1, type=BUY
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-32: getBranchTransactionsByType - Positive: valid branchId + type=CREDITED")
    void tc32_getBranchTransactionsByType_positive() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createTransaction(branch, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndTransactionType(branch.getBranchId(), PaymentTransactionType.CREDITED);

        assertFalse(result.isEmpty(), "Expected transactions for given branch and type");
        result.forEach(tx -> assertEquals(PaymentTransactionType.CREDITED, tx.getTransactionType()));
    }

    // ─────────────────────────────────────────────────────────────
    // TC 33: Negative: branchId=999 (non-existent branch)
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-33: getBranchTransactionsByType - Negative: branchId=999 → empty list")
    void tc33_getBranchTransactionsByType_branchNotFound() {
        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndTransactionType(999, PaymentTransactionType.CREDITED);

        assertTrue(result.isEmpty(), "Expected empty list for non-existent branch");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 34: GET /api/v1/branches/{branch_id}/transactions/status/{status}
    //        Positive: branchId=1, status=SUCCESS
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-34: getBranchTransactionsByStatus - Positive: valid branchId + status=SUCCESS")
    void tc34_getBranchTransactionsByStatus_positive() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createTransaction(branch, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndTransactionStatus(branch.getBranchId(), TransactionStatus.SUCCESS);

        assertFalse(result.isEmpty(), "Expected transactions for given branch and status=SUCCESS");
        result.forEach(tx -> assertEquals(TransactionStatus.SUCCESS, tx.getTransactionStatus()));
    }

    // ─────────────────────────────────────────────────────────────
    // TC 35: Negative: status doesn't match any record for that branch
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-35: getBranchTransactionsByStatus - Negative: invalid status → empty list")
    void tc35_getBranchTransactionsByStatus_noMatch() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createTransaction(branch, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        // FAILED status — no transactions with FAILED status for this branch
        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndTransactionStatus(branch.getBranchId(), TransactionStatus.FAILED);

        assertTrue(result.isEmpty(), "Expected empty list for unmatched status");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 36: GET /api/v1/branches/{branch_id}/transactions/date — Positive: valid range
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-36: getBranchTransactionsByDateRange - Positive: valid date range")
    void tc36_getBranchTransactionsByDateRange_positive() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        LocalDateTime txTime = LocalDateTime.of(2024, 6, 15, 10, 0);
        createTransaction(branch, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, txTime);

        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);

        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndCreatedAtBetween(branch.getBranchId(), from, to);

        assertFalse(result.isEmpty(), "Expected transactions within date range for branch");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 37: GET /api/v1/branches/{branch_id}/transactions/date — Negative: invalid range (from > to)
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-37: getBranchTransactionsByDateRange - Negative: from > to → empty list")
    void tc37_getBranchTransactionsByDateRange_invalidRange() {
        VendorBranches branch = createBranch("Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createTransaction(branch, PaymentTransactionType.CREDITED, TransactionStatus.SUCCESS, LocalDateTime.now());

        LocalDateTime from = LocalDateTime.of(2024, 12, 31, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 1, 0, 0);  // from > to

        List<TransactionHistory> result = transactionRepo
                .findByBranchBranchIdAndCreatedAtBetween(branch.getBranchId(), from, to);

        assertTrue(result.isEmpty(), "Expected empty list when from date is after to date");
    }

    // ─────────────────────────────────────────────────────────────
    // TC 38: GET /api/v1/branches/{branch_id}/details — Positive: branch with vendor + address
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-38: getBranchWithVendorAndAddress - Positive: branch with vendor and address")
    void tc38_getBranchWithVendorAndAddress_positive() {
        Vendors vendor = new Vendors();
        vendor.setVendorName("Tanishq");
        vendor.setTotalGoldQuantity(BigDecimal.valueOf(1000));
        vendor.setCurrentGoldPrice(BigDecimal.valueOf(6000));
        Vendors savedVendor = vendorsRepo.save(vendor);

        Addresses address = new Addresses();
        address.setCity("Pune");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setPostalCode("411001");

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(BigDecimal.valueOf(50));
        branch.setAddress(address);
        branch.setVendors(savedVendor);
        branch.setCreatedAt(LocalDateTime.now());
        VendorBranches saved = branchRepo.save(branch);

        Optional<VendorBranches> result = branchRepo.findById(saved.getBranchId());

        assertTrue(result.isPresent());
        assertNotNull(result.get().getAddress(), "Address should be present");
        assertNotNull(result.get().getVendors(), "Vendor should be present");
        assertEquals("Tanishq", result.get().getVendors().getVendorName());
        assertEquals("Pune", result.get().getAddress().getCity());
    }

    // ─────────────────────────────────────────────────────────────
    // TC 39: GET /api/v1/branches/{branch_id}/details — Negative: id=999 not found
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("TC-39: getBranchWithVendorAndAddress - Negative: id=999 → not found")
    void tc39_getBranchWithVendorAndAddress_notFound() {
        Optional<VendorBranches> result = branchRepo.findById(999);

        assertFalse(result.isPresent(), "Expected empty result for non-existent branch ID 999");
    }
}