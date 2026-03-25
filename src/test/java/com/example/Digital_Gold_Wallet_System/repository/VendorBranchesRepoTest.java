package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VendorBranchesRepoTest {

    @Autowired
    private VendorBranchesRepo repo;

    private VendorBranches createBranch(String city, String state, String country, String postal, BigDecimal qty) {

        Addresses address = new Addresses();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postal);

        VendorBranches branch = new VendorBranches();
        branch.setQuantity(qty);
        branch.setAddressess(address);

        return repo.save(branch);
    }

    @Test
    void testFindAll() {
        createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(20));
        createBranch("Mumbai", "MH", "India", "400001", BigDecimal.valueOf(30));

        List<VendorBranches> list = repo.findAll();
        assertTrue(list.size() >= 2);
    }

    @Test
    void testFindById() {
        VendorBranches b = createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        assertTrue(repo.findById(b.getBranchId()).isPresent());
    }

    @Test
    void testFindByAddressessCity() {
        createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result = repo.findByAddressessCity("Pune");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressessState() {
        createBranch("Nagpur", "MH", "India", "440001", BigDecimal.valueOf(15));

        List<VendorBranches> result = repo.findByAddressessState("MH");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressessCountry() {
        createBranch("Delhi", "Delhi", "India", "110001", BigDecimal.valueOf(40));

        List<VendorBranches> result = repo.findByAddressessCountry("India");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByAddressessPostalCode() {
        createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(25));

        List<VendorBranches> result = repo.findByAddressessPostalCode("411001");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByQuantityBetween() {
        createBranch("A", "MH", "India", "1", BigDecimal.valueOf(10));
        createBranch("B", "MH", "India", "2", BigDecimal.valueOf(50));

        List<VendorBranches> result = repo.findByQuantityBetween(
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(30)
        );

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityAndState() {
        createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result =
                repo.findByAddressessCityAndAddressessState("Pune", "MH");

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityContainingIgnoreCase() {
        createBranch("Pune", "MH", "India", "411001", BigDecimal.valueOf(20));

        List<VendorBranches> result =
                repo.findByAddressessCityContainingIgnoreCase("pun");

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCityOrderByQuantityAsc() {
        createBranch("Pune", "MH", "India", "1", BigDecimal.valueOf(50));
        createBranch("Pune", "MH", "India", "2", BigDecimal.valueOf(10));

        List<VendorBranches> result =
                repo.findByAddressessCityOrderByQuantityAsc("Pune");

        assertTrue(result.get(0).getQuantity()
                .compareTo(result.get(1).getQuantity()) <= 0);
    }

    @Test
    void testFindByCityOrderByQuantityDesc() {
        createBranch("Pune", "MH", "India", "1", BigDecimal.valueOf(10));
        createBranch("Pune", "MH", "India", "2", BigDecimal.valueOf(50));

        List<VendorBranches> result =
                repo.findByAddressessCityOrderByQuantityDesc("Pune");

        assertTrue(result.get(0).getQuantity()
                .compareTo(result.get(1).getQuantity()) >= 0);
    }
}