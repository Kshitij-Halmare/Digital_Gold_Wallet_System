package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VendorBranchesRepoTest {

    @Autowired
    private VendorBranchesRepo repo;

    private VendorBranches createBranch(String street, String city, String state, String country, String postal, BigDecimal qty) {

        Addresses address = new Addresses();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postal);
        address.setStreet(street);

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(qty);
        branch.setAddress(address);

        return repo.save(branch);
    }

    @Test
    void testFindAll() {
        createBranch("101 Avenue","Pune", "MH", "India", "411001", BigDecimal.valueOf(20));
        createBranch("171 Bravo","Mumbai", "MH", "India", "400001", BigDecimal.valueOf(30));

        List<VendorBranches> list = repo.findAll();
        assertTrue(list.size() >= 2);
    }

    @Test
    void testFindById() {
        VendorBranches b = createBranch("171 Bravo","Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        assertTrue(repo.findById(b.getBranchId()).isPresent());
    }

    @Test
    void testFindByAddressCity() {
        createBranch("171 Bravo","Pune", "MH", "India", "411001", BigDecimal.valueOf(20));


        List<VendorBranches> result = repo.findByAddressCity("Pune");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressState() {
        createBranch("171 Bravo","Nagpur", "MH", "India", "440001", BigDecimal.valueOf(15));

        List<VendorBranches> result = repo.findByAddressState("MH");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressCountry() {
        createBranch("171 Bravo","Delhi", "Delhi", "India", "110001", BigDecimal.valueOf(40));

        List<VendorBranches> result = repo.findByAddressCountry("India");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressPostalCode() {
        createBranch("171 Bravo","Pune", "MH", "India", "411001", BigDecimal.valueOf(25));

        List<VendorBranches> result = repo.findByAddressPostalCode("411001");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByQuantityBetween() {
        createBranch("171 Bravo","A", "MH", "India", "1", BigDecimal.valueOf(10));
        createBranch("171 Bravo","B", "MH", "India", "2", BigDecimal.valueOf(50));

        List<VendorBranches> result = repo.findByQuantityBetween(
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(30)
        );

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityAndState() {
        createBranch("171 Bravo","Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result =

                repo.findByAddressCityAndAddressState("Pune", "MH");

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityContainingIgnoreCase() {
        createBranch("171 Bravo","Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result =

                repo.findByAddressCityContainingIgnoreCase("pun");

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityOrderByQuantityAsc() {
        createBranch("171 Bravo","Pune", "MH", "India", "1", BigDecimal.valueOf(50));
        createBranch("171 Bravo","Pune", "MH", "India", "2", BigDecimal.valueOf(10));

        List<VendorBranches> result =

                repo.findByAddressCityOrderByQuantityAsc("Pune");

        assertTrue(result.get(0).getQuantity()
                .compareTo(result.get(1).getQuantity()) <= 0);
    }

    @Test
    void testFindByCityOrderByQuantityDesc() {
        createBranch("171 Bravo","Pune", "MH", "India", "1", BigDecimal.valueOf(10));
        createBranch("171 Bravo","Pune", "MH", "India", "2", BigDecimal.valueOf(50));

        List<VendorBranches> result =

                repo.findByAddressCityOrderByQuantityDesc("Pune");

        assertTrue(result.get(0).getQuantity()
                .compareTo(result.get(1).getQuantity()) >= 0);
    }


    @Autowired
    private VendorsRepo vendorsRepo;

    private Vendors createVendor(String name) {
        Vendors v = new Vendors();
        v.setVendorName(name);
        v.setDescription("Gold Vendor");
        v.setContactPersonName("Rahul");
        v.setContactEmail("rahul@test.com");
        v.setContactPhone("9876543210");
        v.setWebsiteUrl("www.test.com");
        v.setTotalGoldQuantity(new BigDecimal("100.50"));
        v.setCurrentGoldPrice(new BigDecimal("6000.75"));
        v.setCreatedAt(LocalDateTime.now());
        return vendorsRepo.saveAndFlush(v);
    }

    private VendorBranches createBranchWithVendor(Vendors vendor, String city) {

        Addresses address = new Addresses();
        address.setCity(city);
        address.setState("MH");
        address.setCountry("India");
        address.setPostalCode("000000");
        address.setStreet("Test Street");

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(BigDecimal.valueOf(10));
        branch.setAddress(address);
        branch.setVendors(vendor);

        return repo.save(branch);
    }

    @Test
    void testFindByVendorId() {

        Vendors vendor = createVendor("Tanishq");

        createBranchWithVendor(vendor, "Pune");
        createBranchWithVendor(vendor, "Mumbai");

        List<VendorBranches> result =
                repo.findByVendorsVendorId(vendor.getVendorId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByVendorId_NotFound() {

        List<VendorBranches> result =
                repo.findByVendorsVendorId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testBranchesMappedToCorrectVendor() {

        Vendors v1 = createVendor("Tanishq");
        Vendors v2 = createVendor("Kalyan");

        createBranchWithVendor(v1, "Pune");
        createBranchWithVendor(v2, "Delhi");

        List<VendorBranches> result =
                repo.findByVendorsVendorId(v1.getVendorId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pune", result.get(0).getAddress().getCity());
    }
}