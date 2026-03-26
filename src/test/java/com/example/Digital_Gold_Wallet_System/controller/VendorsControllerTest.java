package com.example.Digital_Gold_Wallet_System.controller;

import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class VendorsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testCreateVendor() throws Exception{
        String addVendorJson = """
                    "vendorName":"Tanishq CO.",
                    "description":"Best in NGP",
                    "contactPersonName":"Aman",
                    "contactEmail":"aman@gmail.com",
                    "contactPhone":"6577362454",
                    "websiteUrl":,
                    "totalGoldQuantity":,
                    "currentGoldPrice":
                """
    }

}
