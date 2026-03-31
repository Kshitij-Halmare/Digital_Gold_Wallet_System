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
import java.util.List;

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
        List<Vendors> vendors = vendorRepo.findAll();
        vendorRepo.save(createVendor("Tanishq"));
        vendorRepo.save(createVendor("Kalyan"));

        mockMvc.perform(get("/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors.length()").value(vendors.size()+2));
    }

//    @Test
//    void tc2_getAllVendors_empty() throws Exception {
//        mockMvc.perform(get("/vendors"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("_embedded.vendors").isEmpty());
//    }

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
                .andExpect(status().isInternalServerError());
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
                .andExpect(status().isInternalServerError());
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
                .andExpect(status().isInternalServerError());
    }

    @Test
    void tc11_update_invalid() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("XYZ"));

        mockMvc.perform(put("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"vendorName\": \"\" }"))
                .andExpect(status().isNoContent()); // correct SDR behavior
    }

    private Vendors createVendorWithDetails(String name, BigDecimal price, BigDecimal quantity) {
        Vendors v = new Vendors();
        v.setVendorName(name);
        v.setDescription("Gold vendor");
        v.setContactPersonName("John Doe");
        v.setContactEmail("john@example.com");
        v.setContactPhone("9876543210");
        v.setWebsiteUrl("https://example.com");
        v.setTotalGoldQuantity(quantity);
        v.setCurrentGoldPrice(price);
        v.setCreatedAt(LocalDateTime.now());
        return v;
    }

    @Test
    @DisplayName("Search by name - partial keyword match")
    void tc12_searchByName_partialMatch() throws Exception{
        vendorRepo.save(createVendorWithDetails("Tanishq Premium", new BigDecimal("6100.00"), new BigDecimal("120.00")));
        vendorRepo.save(createVendorWithDetails("Tanishq Lite",    new BigDecimal("5800.00"), new BigDecimal("60.00")));

        mockMvc.perform(get("/vendors/search/findByVendorNameContainingIgnoreCase").param("keyword","tanishq"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors").isNotEmpty());
    }

    @Test
    @DisplayName("Search by name - case insensitive uppercase input")
    void tc13_searchByName_caseInsensitive() throws Exception {
        vendorRepo.save(createVendorWithDetails("GoldMart Elite", new BigDecimal("5900.00"), new BigDecimal("80.00")));

        mockMvc.perform(get("/vendors/search/findByVendorNameContainingIgnoreCase")
                        .param("keyword", "GOLDMART"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray());
    }

    @Test
    @DisplayName("Search by name - no match returns empty")
    void tc14_searchByName_noMatch() throws Exception{
        mockMvc.perform(get("/vendors/search/findByVendorNameContainingIgnoreCase").param("keyword","ZZMOYF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isEmpty());
    }

    @Test
    @DisplayName("Filter by price range ")
    void tc15_filterByPriceRange_found() throws Exception{
        vendorRepo.save(createVendorWithDetails("BudgetGold",  new BigDecimal("5500.00"), new BigDecimal("50.00")));
        vendorRepo.save(createVendorWithDetails("PremiumGold", new BigDecimal("7500.00"), new BigDecimal("200.00")));

        mockMvc.perform(get("/vendors/search/findByCurrentGoldPriceBetween")
                        .param("minPrice", "5000")
                        .param("maxPrice", "6500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors").isNotEmpty());
    }

    @Test
    @DisplayName("Filter by price range - no vendors in range")
    void tc16_filterByPriceRange_notFound() throws Exception {
        vendorRepo.save(createVendorWithDetails("ExpensiveVendor", new BigDecimal("9999.00"), new BigDecimal("50.00")));

        mockMvc.perform(get("/vendors/search/findByCurrentGoldPriceBetween")
                        .param("minPrice", "1000")
                        .param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isEmpty());
    }

    @Test
    @DisplayName("Filter by max price - vendors below or equal to max returned")
    void tc17_filterByMaxPrice_found() throws Exception {
        vendorRepo.save(createVendorWithDetails("AffordableGold", new BigDecimal("5000.00"), new BigDecimal("40.00")));
        vendorRepo.save(createVendorWithDetails("ExpensiveGold",  new BigDecimal("8000.00"), new BigDecimal("300.00")));

        mockMvc.perform(get("/vendors/search/findByCurrentGoldPriceLessThanEqual")
                        .param("maxPrice", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors").isNotEmpty());
    }

    @Test
    @DisplayName("Filter by min price - vendors above or equal to min returned")
    void tc18_filterByMinPrice_found() throws Exception {
        vendorRepo.save(createVendorWithDetails("LuxuryGold", new BigDecimal("8500.00"), new BigDecimal("500.00")));
        vendorRepo.save(createVendorWithDetails("CheapGold",  new BigDecimal("4000.00"), new BigDecimal("20.00")));

        mockMvc.perform(get("/vendors/search/findByCurrentGoldPriceGreaterThanEqual")
                        .param("minPrice", "8000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors").isNotEmpty());
    }

    @Test
    @DisplayName("Filter by quantity range - vendors within range returned")
    void tc19_filterByQuantityRange_found() throws Exception {
        vendorRepo.save(createVendorWithDetails("SmallVendor",  new BigDecimal("6000.00"), new BigDecimal("10.00")));
        vendorRepo.save(createVendorWithDetails("MediumVendor", new BigDecimal("6100.00"), new BigDecimal("100.00")));
        vendorRepo.save(createVendorWithDetails("LargeVendor",  new BigDecimal("6200.00"), new BigDecimal("500.00")));

        mockMvc.perform(get("/vendors/search/findByTotalGoldQuantityBetween")
                        .param("minQty", "50")
                        .param("maxQty", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isArray())
                .andExpect(jsonPath("_embedded.vendors").isNotEmpty());
    }

    @Test
    @DisplayName("Filter by quantity range - no vendors found")
    void tc20_filterByQuantityRange_notFound() throws Exception {
        vendorRepo.save(createVendorWithDetails("TinyVendor", new BigDecimal("6000.00"), new BigDecimal("5.00")));

        mockMvc.perform(get("/vendors/search/findByTotalGoldQuantityBetween")
                        .param("minQty", "900")
                        .param("maxQty", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vendors").isEmpty());
    }


    @Test
    void patchVendor_updateCurrentGoldPrice() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("Tanishq"));

        String patchJson = "{ \"currentGoldPrice\": 7500.00 }";

        mockMvc.perform(patch("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNoContent());
    }


    @Test
    void patchVendor_verifyCurrentGoldPriceUpdated() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("Tanishq"));

        String patchJson = "{ \"currentGoldPrice\": 7500.00 }";

        // First PATCH it
        mockMvc.perform(patch("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNoContent());

        // Then GET and verify the new price
        mockMvc.perform(get("/vendors/" + saved.getVendorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentGoldPrice").value(7500.00))
                .andExpect(jsonPath("$.vendorName").value("Tanishq")); // other fields unchanged
    }


    /*@Test
    void patchVendor_notFound() throws Exception {
        String patchJson = "{ \"currentGoldPrice\": 7500.00 }";

        mockMvc.perform(patch("/vendors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNotFound());
    }*/


    @Test
    void patchVendor_negativePrice_invalid() throws Exception {
        Vendors saved = vendorRepo.save(createVendor("Tanishq"));

        String patchJson = "{ \"currentGoldPrice\": -100.00 }";

        mockMvc.perform(patch("/vendors/" + saved.getVendorId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNoContent());
    }
}
