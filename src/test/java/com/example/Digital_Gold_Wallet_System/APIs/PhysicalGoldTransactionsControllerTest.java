package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.*;
import com.example.Digital_Gold_Wallet_System.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PhysicalGoldTransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorBranchesRepo branchRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private AddressesRepo addressesRepo;

    @Autowired
    private VendorsRepo vendorsRepo;


    @Autowired
    private PhysicalGoldTransactionsRepo physicalGoldTransactionsRepo;

    private PhysicalGoldTransactions createTransaction(BigDecimal quantity, LocalDateTime createdAt) {

        // Step 1 — Create Address (no dependencies)
        Addresses address = new Addresses();
        address.setStreet("MG Road");
        address.setCity("Mumbai");
        address.setState("Maharashtra");
        address.setPostalCode("411001");
        address.setCountry("India");
        Addresses savedAddress = addressesRepo.save(address);

        // Step 2 — Create Vendor (no dependencies)
        Vendors vendor = new Vendors();
        vendor.setVendorName("Test Vendor");
        vendor.setDescription("Test");
        vendor.setContactPersonName("Test Person");
        vendor.setContactEmail("test@vendor.com");
        vendor.setContactPhone("9876543210");
        vendor.setWebsiteUrl("www.test.com");
        vendor.setTotalGoldQuantity(new BigDecimal("100.00"));
        vendor.setCurrentGoldPrice(new BigDecimal("6000.00"));
        vendor.setCreatedAt(LocalDateTime.now());
        Vendors savedVendor = vendorsRepo.save(vendor);

        // Step 3 — Create VendorBranch (depends on Vendor + Address)
        VendorBranches branch = new VendorBranches();
        branch.setQuantity(new BigDecimal("50.00"));
        branch.setCreatedAt(LocalDateTime.now());
        branch.setVendors(savedVendor);
        branch.setAddress(savedAddress);
        VendorBranches savedBranch = branchRepo.save(branch);

        // Step 4 — Create User (depends on Address)
        Users user = new Users();
        user.setName("Test User");
        user.setEmail("testuser" + System.currentTimeMillis() + "@test.com"); // unique email
        user.setBalance(new BigDecimal("1000.00"));
        user.setAddress(savedAddress);
        Users savedUser = usersRepo.save(user);

        // Step 5 — Create PhysicalGoldTransaction (depends on Branch + User + Address)
        PhysicalGoldTransactions t = new PhysicalGoldTransactions();
        t.setQuantity(quantity);
        t.setCreatedAt(createdAt);
        t.setBranch(savedBranch);
        t.setUser(savedUser);
        t.setDeliveryAddress(savedAddress);
        return physicalGoldTransactionsRepo.save(t);
    }

    @Test
    void testFindByBranchId() throws Exception{
        //Positive scenario: when the branch Id exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByBranch_BranchId").param("branchId","1")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testFindByBranchId_NegativeValue() throws Exception{
        //Positive scenario: when the branch Id is negative
        mockMvc.perform(get("/physicalgoldtransaction/search/findByBranch_BranchId").param("branchId","-10")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByBranchId_NotFound() throws Exception{
        //Negative scenario: when the branch id does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByBranch_BranchId").param("branchId","999")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetAllPhysicalGoldTransaction() throws Exception{
        mockMvc.perform(get("/physicalgoldtransaction")).andExpect(status().isOk());
    }

    @Test
    void testGetByQuantity() throws Exception{
        //Positive Scenario:to get by quantity
        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantity").param("quantity","100")).andExpect(status().isOk());
    }

    @Test
    void testGetByQuantity_NegativeValue() throws Exception{
        //Negative scenario

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantity").param("quantity","-10")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetByDeliveryAddress_City() throws Exception {
        // Positive scenario: city exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_City")
                        .param("city", "Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByDeliveryAddress_City_NotFound() throws Exception {
        // Negative scenario: city does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_City")
                        .param("city", "UnknownCity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetByDeliveryAddress_Street() throws Exception {
        // Positive scenario: street exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_Street")
                        .param("street", "MG Road"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByDeliveryAddress_Street_NotFound() throws Exception {
        // Negative scenario: street does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_Street")
                        .param("street", "Unknown Street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetByDeliveryAddress_PostalCode() throws Exception {
        // Positive scenario: postal code exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_PostalCode")
                        .param("postalCode", "411001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByDeliveryAddress_PostalCode_notFound() throws Exception {
        // Negative scenario: postal code does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_PostalCode")
                        .param("postalCode", "0000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }


    @Test
    void testGetByDeliveryAddress_State() throws Exception {
        // Positive scenario: state exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_State")
                        .param("state", "Maharashtra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByDeliveryAddress_State_NotFound() throws Exception {
        // Negative scenario: state does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_State")
                        .param("state", "UnknownState"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetByDeliveryAddress_Country() throws Exception {
        // Positive scenario: country exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_Country")
                        .param("country", "India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByDeliveryAddress_Country_NotFound() throws Exception {
        // Negative scenario: country does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByDeliveryAddress_Country")
                        .param("country", "UnknownCountry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testGetByUserName() throws Exception {
        // Positive scenario: user exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByUser_Name")
                        .param("name", "Sanika Jain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").exists());
    }

    @Test
    void testGetByUserName_NotFound() throws Exception {
        // Negative scenario: user does not exist
        mockMvc.perform(get("/physicalgoldtransaction/search/findByUser_Name")
                        .param("name", "abcd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByQuantityBetween_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityBetween")
                        .param("minQty", "50")
                        .param("maxQty", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByQuantityBetween_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityBetween")
                        .param("minQty", "9000")
                        .param("maxQty", "9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByQuantityGreaterThanEqual_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());  // ← inserts own data

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityGreaterThanEqual")
                        .param("minQty", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByQuantityGreaterThanEqual_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityGreaterThanEqual")
                        .param("minQty", "99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByQuantityLessThanEqual_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityLessThanEqual")
                        .param("maxQty", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByQuantityLessThanEqual_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.now());

        mockMvc.perform(get("/physicalgoldtransaction/search/findByQuantityLessThanEqual")
                        .param("maxQty", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByCreatedAtBetween_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtBetween")
                        .param("from", "2024-01-01T00:00:00")
                        .param("to",   "2026-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByCreatedAtBetween_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtBetween")
                        .param("from", "2000-01-01T00:00:00")
                        .param("to",   "2000-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByCreatedAtAfter_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtAfter")
                        .param("from", "2024-01-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByCreatedAtAfter_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtAfter")
                        .param("from", "2099-01-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }

    @Test
    void testFindByCreatedAtBefore_Found() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtBefore")
                        .param("to", "2099-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isNotEmpty());
    }

    @Test
    void testFindByCreatedAtBefore_NotFound() throws Exception {
        createTransaction(new BigDecimal("100"), LocalDateTime.of(2025, 6, 15, 10, 0));

        mockMvc.perform(get("/physicalgoldtransaction/search/findByCreatedAtBefore")
                        .param("to", "2000-01-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isArray())
                .andExpect(jsonPath("$._embedded.physicalGoldTransactionses").isEmpty());
    }







}
