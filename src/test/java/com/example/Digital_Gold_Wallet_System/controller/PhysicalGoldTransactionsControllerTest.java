package com.example.Digital_Gold_Wallet_System.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PhysicalGoldTransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*@Test
    void testFindByBranchId() throws Exception{
        //Positive scenario: when the branch Id exists
        mockMvc.perform(get("/physicalgoldtransaction/search/findByBranchId").param("branchId","1")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded").exists());
    }*/

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










}
