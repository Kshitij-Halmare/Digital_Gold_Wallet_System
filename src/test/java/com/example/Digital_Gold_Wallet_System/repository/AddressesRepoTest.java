package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AddressesRepoTest {

    @Autowired
    AddressesRepo addressRepository;

    @Test
    @DisplayName("Save valid address")
    void testSaveValidAddress() {

        Addresses address = new Addresses();
        address.setCity("Nagpur");
        address.setState("Maharashtra");
        address.setCountry("India");

        Addresses savedAddress = addressRepository.save(address);

        assertNotNull(savedAddress);
        assertNotNull(savedAddress.getAddressId());
        assertEquals("Nagpur", savedAddress.getCity());
    }

    @Test
    @DisplayName("Find address by ID")
    void testFindAddressById() {

        Addresses address = new Addresses();
        address.setCity("Mumbai");
        address.setState("Maharashtra");
        address.setCountry("India");

        Addresses savedAddress = addressRepository.save(address);

        Optional<Addresses> foundAddress =
                addressRepository.findById(savedAddress.getAddressId());

        assertTrue(foundAddress.isPresent());
        assertEquals("Mumbai", foundAddress.get().getCity());
    }

}

