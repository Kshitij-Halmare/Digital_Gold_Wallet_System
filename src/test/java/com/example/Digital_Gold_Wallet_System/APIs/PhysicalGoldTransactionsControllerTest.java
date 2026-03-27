package com.example.Digital_Gold_Wallet_System.APIs;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PhysicalGoldTransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

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













}
