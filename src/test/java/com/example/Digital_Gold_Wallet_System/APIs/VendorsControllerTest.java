package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Vendors;
import com.example.Digital_Gold_Wallet_System.repository.VendorsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VendorsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private VendorsRepo vendorRepo;

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

    private String getVendorJson(String name) {
        return "{ " +
                "\"vendorName\":\"" + name + "\"," +
                "\"description\":\"Gold vendor\"," +
                "\"contactPersonName\":\"John Doe\"," +
                "\"contactEmail\":\"john@example.com\"," +
                "\"contactPhone\":\"9876543210\"," +
                "\"websiteUrl\":\"https://example.com\"," +
                "\"totalGoldQuantity\":100," +
                "\"currentGoldPrice\":5000," +
                "\"createdAt\":\"2026-01-01T10:00:00\"" +
                "}";
    }

    @Test
    void tc1_getAllVendors_positive() throws Exception {
        vendorRepo.save(createVendor("Tanishq"));
        vendorRepo.save(createVendor("Kalyan"));

        mockMvc.perform(get("/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors.length()").value(2));
    }

    @Test
    void tc2_getAllVendors_empty() throws Exception {
        mockMvc.perform(get("/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isEmpty());
    }

    @Test
    @DisplayName("Get vendor by ID - success")
    void tc3_getVendorById_positive() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("Tanishq"));

        mockMvc.perform(get("/vendors/" + saved.getVendorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorName").value("Tanishq"));
    }

    @Test
    @DisplayName("Get vendor by ID - not found")
    void tc4_getVendorById_notFound() throws Exception {
        mockMvc.perform(get("/vendors/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tc5_searchVendorByName() throws Exception {
        vendorRepo.save(createVendor("Tanishq"));

        mockMvc.perform(get("/vendors/search/findByVendorName")
                        .param("vendorName", "Tanishq"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray());
    }


    @Test
    @DisplayName("Create vendor - success")
    void tc6_addVendor_positive() throws Exception {
        mockMvc.perform(post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getVendorJson("Tanishq")))
                .andExpect(status().isCreated());
    }

    @Test
    void tc7_addVendor_invalid() throws Exception {
        String invalidJson = "{ \"vendorName\": \"\" }";

        mockMvc.perform(post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tc9_updateVendor() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("Old"));

        mockMvc.perform(put("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getVendorJson("Updated")))
                .andExpect(status().isNoContent());
    }

    @Test
    void tc10_updateVendor_notFound() throws Exception {
        mockMvc.perform(put("/vendors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getVendorJson("Test")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tc11_update_invalid() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("XYZ"));

        mockMvc.perform(put("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"vendorName\": \"\" }"))
                .andExpect(status().isNoContent()); // correct SDR behavior
    }
}
