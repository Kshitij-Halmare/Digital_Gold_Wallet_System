package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.Projection.VendorBranchProjection;
import com.example.Digital_Gold_Wallet_System.Projection.VendorBranchesProjection;
import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.TransactionHistory;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;

import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionStatus;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;
import com.example.Digital_Gold_Wallet_System.repository.AddressesRepo;
import com.example.Digital_Gold_Wallet_System.repository.TransactionHistoryRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Autowired
    MockMvc mockMvc;

    private VendorBranches createBranch(Vendors vendor, String street, String city, String state, String country,
                                        String postal, BigDecimal qty) {

        if (vendor != null) {
            vendor = vendorsRepo.saveAndFlush(vendor);
        }

        Addresses address = new Addresses();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postal);

        address = addressRepo.saveAndFlush(address); // ✅ FIX

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(qty);
        branch.setAddress(address);
        branch.setVendors(vendor);
        branch.setCreatedAt(LocalDateTime.now());

        return branchRepo.saveAndFlush(branch); // ✅ FIX
    }

    // ✅ OVERLOADED METHOD (for old tests)
    private VendorBranches createBranch(String street, String city, String state,
                                        String country, String postal, BigDecimal qty) {
        return createBranch(null, street, city, state, country, postal, qty);
    }
    private Vendors createVendor(String name) {
        Vendors v = new Vendors();
        v.setVendorName(name);
        v.setDescription("Gold vendor");
        v.setContactPersonName("John Doe");
        v.setContactEmail("john@example.com");
        v.setContactPhone("9876543210");
        v.setWebsiteUrl("https://example.com");
        v.setTotalGoldQuantity(BigDecimal.valueOf(100));
        v.setCurrentGoldPrice(BigDecimal.valueOf(5000));
        v.setCreatedAt(LocalDateTime.now());
        return v;
    }

    private TransactionHistory createTransaction(VendorBranches branch,
                                                 TransactionType type,
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

    // ================= BASIC TESTS =================

//    @Test
//    void tc2_getAllBranches_negative_empty() {
//        List<VendorBranches> result = branchRepo.findAll();
//        assertTrue(result.isEmpty());
//    }

    @Test
    void tc1_getAllBranches_positive() {
        createBranch("MG Road","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createBranch("MG Road","Mumbai", "Maharashtra", "India", "400001", BigDecimal.valueOf(30));

        List<VendorBranches> result = branchRepo.findAll();
        assertFalse(result.isEmpty());
    }

//    @Test
//    void tc3_getBranchesByCity_positive() {
//        createBranch("MG Road","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));
//
//        List<VendorBranches> result = branchRepo.findByAddressCity("Pune");
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void tc4_getBranchesByCity_cityNotFound() {
//        List<VendorBranches> result = branchRepo.findByAddressCity("XYZ");
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void tc6_getBranchesByState_positive() {
//        createBranch("MG Road","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));
//
//        List<VendorBranches> result = branchRepo.findByAddressState("Maharashtra");
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void tc8_getBranchesByCountry_positive() {
//        createBranch("MG Road","Delhi", "Delhi", "India", "110001", BigDecimal.valueOf(40));
//
//        List<VendorBranches> result = branchRepo.findByAddressCountry("India");
//        assertFalse(result.isEmpty());
//    }

//    @Test
//    void tc10_getBranchesByPostalCode_positive() {
//        createBranch("MG Road","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(25));
//
//        List<VendorBranches> result = branchRepo.findByAddressPostalCode("411001");
//        assertFalse(result.isEmpty());
//    }

    @Test
    void tc15_getBranchById_positive() {
        VendorBranches saved = createBranch("MG Road","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        Optional<VendorBranches> result = branchRepo.findById(saved.getBranchId());
        assertTrue(result.isPresent());
    }

    // ================= VENDOR BASED TESTS =================
    @Test
    @DisplayName("TC-18: Get branches by vendorId - success")
    void tc18_getBranchesByVendorId_positive() throws Exception {

        Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));

        createBranch(vendor,"MG Road", "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));

        mockMvc.perform(get("/vendorBranches/search/findByVendorsVendorId")
                        .param("vendorId", vendor.getVendorId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendorBranches").isArray())
                .andExpect(jsonPath("_embedded.vendorBranches.length()").value(1));
    }

    @Test
    @DisplayName("TC-19: Get branches by vendorId - no data")
    void tc19_getBranchesByVendorId_notFound() throws Exception {

        mockMvc.perform(get("/vendorBranches/search/findByVendorsVendorId")
                        .param("vendorId", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendorBranches").isEmpty());
    }


}