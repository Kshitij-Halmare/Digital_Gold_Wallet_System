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

    @Test
    void testSaveVendor_AllFields() {

        Vendors v = new Vendors();
        v.setVendorName("Tanishq");
        v.setDescription("Gold Vendor");
        v.setContactPersonName("Rahul");
        v.setContactEmail("rahul@test.com");
        v.setContactPhone("9876543210");
        v.setWebsiteUrl("www.tanishq.com");
        v.setTotalGoldQuantity(new BigDecimal("100.50"));
        v.setCurrentGoldPrice(new BigDecimal("6000.75"));
        v.setCreatedAt(LocalDateTime.now());

        Vendors saved = vendorsRepo.save(v);

        assertNotNull(saved.getVendorId());
        assertEquals("Tanishq", saved.getVendorName());
    }

    @Test
    void testFindAll_VendorsExist() {

        List<Vendors> vendors = vendorsRepo.findAll();

        Vendors v1 = new Vendors();
        v1.setVendorName("Tanishq");
        v1.setDescription("Gold Vendor");
        v1.setContactPersonName("Rahul");
        v1.setContactEmail("rahul@test.com");
        v1.setContactPhone("9876543210");
        v1.setWebsiteUrl("www.tanishq.com");
        v1.setTotalGoldQuantity(new BigDecimal("100.50"));
        v1.setCurrentGoldPrice(new BigDecimal("6000.75"));
        v1.setCreatedAt(LocalDateTime.now());

        Vendors v2 = new Vendors();
        v2.setVendorName("Kalyan");
        v2.setDescription("Gold Vendor");
        v2.setContactPersonName("Rahul");
        v2.setContactEmail("rahul@test.com");
        v2.setContactPhone("9876543210");
        v2.setWebsiteUrl("www.tanishq.com");
        v2.setTotalGoldQuantity(new BigDecimal("100.50"));
        v2.setCurrentGoldPrice(new BigDecimal("6000.75"));
        v2.setCreatedAt(LocalDateTime.now());

        vendorsRepo.save(v1);
        vendorsRepo.save(v2);

        List<Vendors> result = vendorsRepo.findAll();

        assertEquals(vendors.size()+2, result.size());
    }


    @Test
    void testFindByVendorName_Found() {

        Vendors v = new Vendors();
        v.setVendorName("Malabar");
        v.setDescription("Gold Vendor");
        v.setContactPersonName("Rahul");
        v.setContactEmail("rahul@test.com");
        v.setContactPhone("9876543210");
        v.setWebsiteUrl("www.tanishq.com");
        v.setTotalGoldQuantity(new BigDecimal("100.50"));
        v.setCurrentGoldPrice(new BigDecimal("6000.75"));
        v.setCreatedAt(LocalDateTime.now());

        vendorsRepo.save(v);

        List<Vendors> result = vendorsRepo.findByVendorName("Malabar");

        assertFalse(result.isEmpty());
        assertEquals("Malabar", result.get(0).getVendorName());
    }


    @Test
    void testFindByVendorName_NotFound() {

        List<Vendors> result = vendorsRepo.findByVendorName("XYZ");

        assertTrue(result.isEmpty());
    }


}
