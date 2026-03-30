package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VendorsRepoTest {

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
        return v;
    }

    @Test
    void testSaveVendor_AllFields() {

        Vendors saved = vendorsRepo.save(createVendor("Tanishq"));

        assertNotNull(saved.getVendorId());
        assertEquals("Tanishq", saved.getVendorName());
    }

    @Test
    void testFindAll_VendorsExist() {

        vendorsRepo.save(createVendor("Tanishq"));
        vendorsRepo.save(createVendor("Kalyan"));

        List<Vendors> vendors = vendorsRepo.findAll();

        List<Vendors> result = vendorsRepo.findAll();

        assertEquals(vendors.size(), result.size());
    }

    @Test
    void testFindAll_Empty() {
        List<Vendors> result = vendorsRepo.findAll();
        assertTrue(result.isEmpty());
    }


    @Test
    void testFindByVendorName_Found() {

        vendorsRepo.save(createVendor("Malabar"));

        List<Vendors> result = vendorsRepo.findByVendorName("Malabar");

        assertFalse(result.isEmpty());
        assertEquals("Malabar", result.get(0).getVendorName());
    }


    @Test
    void testFindByVendorName_NotFound() {

        List<Vendors> result = vendorsRepo.findByVendorName("XYZ");

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateVendor() {
        Vendors saved = vendorsRepo.save(createVendor("OldName"));

        saved.setVendorName("UpdatedName");
        vendorsRepo.save(saved);

        Vendors updated = vendorsRepo.findById(saved.getVendorId()).orElse(null);

        assertNotNull(updated);
        assertEquals("UpdatedName", updated.getVendorName());
    }

    @Test
    void testSaveVendor_MinimalFields() {
        Vendors v = new Vendors();
        v.setVendorName("Minimal");
        v.setCreatedAt(LocalDateTime.now());

        Vendors saved = vendorsRepo.save(v);

        assertNotNull(saved.getVendorId());
        assertEquals("Minimal", saved.getVendorName());
    }



}
