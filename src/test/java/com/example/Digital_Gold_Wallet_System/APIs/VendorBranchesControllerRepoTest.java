package com.example.Digital_Gold_Wallet_System.APIs;

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

    private VendorBranches createBranch(Vendors vendor,String street, String city, String state, String country,
                                        String postal, BigDecimal qty) {

        if (vendor != null) {
            vendor = vendorsRepo.saveAndFlush(vendor);
        }

        Addresses address = new Addresses();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postal);
        address.setStreet(street);

        address = addressRepo.saveAndFlush(address); // ✅ FIX

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(qty);
        branch.setAddress(address);
        branch.setVendors(vendor);
        branch.setCreatedAt(LocalDateTime.now());

        return branchRepo.saveAndFlush(branch); // ✅ FIX
    }

    // ✅ OVERLOADED METHOD (for old tests)
    private VendorBranches createBranch(String street, String city, String state, String country,
                                        String postal, BigDecimal qty) {
        return createBranch(null,street, city, state, country, postal, qty);
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

    // ================= BASIC TESTS =================

    @Test
    void tc1_getAllBranches_positive() {
        createBranch("CA","Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
        createBranch("CA", "Mumbai", "Maharashtra", "India", "400001", BigDecimal.valueOf(30));

        List<VendorBranches> result = branchRepo.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void tc2_getAllBranches_negative_empty() {
        List<VendorBranches> result = branchRepo.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void tc3_getBranchesByCity_positive() {
        createBranch("CA", "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result = branchRepo.findByAddressCity("Pune");
        assertFalse(result.isEmpty());
    }

    @Test
    void tc4_getBranchesByCity_cityNotFound() {
        List<VendorBranches> result = branchRepo.findByAddressCity("XYZ");
        assertTrue(result.isEmpty());
    }

    @Test
    void tc6_getBranchesByState_positive() {
        createBranch("CA", "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result = branchRepo.findByAddressState("Maharashtra");
        assertFalse(result.isEmpty());
    }

    @Test
    void tc8_getBranchesByCountry_positive() {
        createBranch("CA", "Delhi", "Delhi", "India", "110001", BigDecimal.valueOf(40));

        List<VendorBranches> result = branchRepo.findByAddressCountry("India");
        assertFalse(result.isEmpty());
    }

    @Test
    void tc10_getBranchesByPostalCode_positive() {
        createBranch("CA", "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(25));

        List<VendorBranches> result = branchRepo.findByAddressPostalCode("411001");
        assertFalse(result.isEmpty());
    }

    @Test
    void tc15_getBranchById_positive() {
        VendorBranches saved = createBranch("CA", "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(20));

        Optional<VendorBranches> result = branchRepo.findById(saved.getBranchId());
        assertTrue(result.isPresent());
    }

    // ================= VENDOR BASED TESTS =================

//    @Test
//    void tc18_getBranchesByVendorId_positive() {
//        Vendors vendor = vendorsRepo.save(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result = branchRepo.findByVendorsVendorId(vendor.getVendorId());
//        assertFalse(result.isEmpty());
//    }

    @Test
    void tc19_getBranchesByVendorId_notFound() {
        List<VendorBranches> result = branchRepo.findByVendorsVendorId(999);
        assertTrue(result.isEmpty());
    }

//    @Test
//    void tc20_getBranchesByVendorId_city_positive() {
//        Vendors vendor = vendorsRepo.save(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressCity(vendor.getVendorId(), "Pune");
//
//        assertFalse(result.isEmpty());
//    }

//    @Test
//    void tc23_getBranchesByVendorId_state_positive() {
//        Vendors vendor = vendorsRepo.save(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressState(vendor.getVendorId(), "Maharashtra");
//
//        assertFalse(result.isEmpty());
//    }

//    @Test
//    void tc25_getBranchesByVendorId_country_positive() {
//        Vendors vendor = vendorsRepo.save(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressCountry(vendor.getVendorId(), "India");
//
//        assertFalse(result.isEmpty());
//    }
//@Test
//void tc18_getBranchesByVendorId_positive() {
//    Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));
//
//    createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//    List<VendorBranches> result =
//            branchRepo.findByVendorsVendorId(vendor.getVendorId());
//
//    assertFalse(result.isEmpty());
//}
//    @Test
//    void tc20_getBranchesByVendorId_city_positive() {
//        Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressCity(vendor.getVendorId(), "Pune");
//
//        assertFalse(result.isEmpty());
//    }
//    @Test
//    void tc21_getBranchesByVendorId_city_negative() {
//        Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressCity(vendor.getVendorId(), "XYZ");
//
//        assertTrue(result.isEmpty());
//    }
//    @Test
//    void tc24_getBranchesByVendorId_state_negative() {
//        Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressState(vendor.getVendorId(), "ABC");
//
//        assertTrue(result.isEmpty());
//    }
//    @Test
//    void tc25_getBranchesByVendorId_country_positive() {
//        Vendors vendor = vendorsRepo.saveAndFlush(createVendor("Tanishq"));
//
//        createBranch(vendor, "Pune", "Maharashtra", "India", "411001", BigDecimal.valueOf(50));
//
//        List<VendorBranches> result =
//                branchRepo.findByVendorsVendorIdAndAddressCountry(vendor.getVendorId(), "India");
//
//        assertFalse(result.isEmpty());
//    }
}