package com.example.Digital_Gold_Wallet_System.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VendorsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testCreateVendor() throws Exception{
        String addVendorJson = """
                    {
                        "vendorName":"Tanishq CO.",
                        "description":"Best in NGP",
                        "contactPersonName":"Aman",
                        "contactEmail":"aman@gmail.com",
                        "contactPhone":"6577362454",
                        "websiteUrl":"tanishq.com",
                        "totalGoldQuantity":"500",
                        "currentGoldPrice":"43500"
                }
                """;

        mockMvc.perform(post("/vendors")
                .contentType("application/json")
                .content(addVendorJson))
                .andExpect(status().isCreated());

    }

}
