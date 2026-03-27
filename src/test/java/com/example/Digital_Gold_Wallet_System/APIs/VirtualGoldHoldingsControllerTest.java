package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import com.example.Digital_Gold_Wallet_System.repository.VirtualGoldHoldingsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VirtualGoldHoldingsControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VirtualGoldHoldingsRepo repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }


    @Test
    void testGetAllHoldings_Positive() throws Exception{
        VirtualGoldHoldings h = new VirtualGoldHoldings();
        h.setQuantity(BigDecimal.valueOf(10));
        h.setCreatedAt(LocalDateTime.now());
        repository.save(h);

        mockMvc.perform(get("/virtual_gold_holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.holdings").exists());
    }

    @Test
    void testGetAllHoldings_Empty() throws Exception{
        mockMvc.perform(get("/virtual_gold_holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.holdings").isEmpty());
    }

    @Test
    void testGetByBranchId() throws Exception{
        //positive scenario:branch Id exists
        mockMvc.perform(get("/virtual_gold_holdings/search/findByBranch_BranchId").param("branchId","1")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").exists());
    }

    @Test
    void testGetByBranchId_NotFound() throws Exception{
        //positive scenario:branch Id does not exists
        mockMvc.perform(get("/virtual_gold_holdings/search/findByBranch_BranchId").param("branchId","999")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").isEmpty());
    }

    @Test
    void testGetByQuantity() throws Exception{
        //positive Scenario: quantity exists

        mockMvc.perform(get("/virtual_gold_holdings/search/findByQuantity").param("quantity","10")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").exists());
    }

    @Test
    void testGetByQuantity_NegativeQuantity() throws Exception{
        //negative Scenario: quantity does not exists

        mockMvc.perform(get("/virtual_gold_holdings/search/findByQuantity").param("quantity","-10")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").isEmpty());
    }

    @Test
    void testGetByUser_Positive() throws Exception{
        mockMvc.perform(get("/virtual_gold_holdings/search/findByUser_UserId").param("userId","1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").exists());
    }

    @Test
    void testGetByUser_Empty() throws Exception{
        mockMvc.perform(get("/virtual_gold_holdings/search/findByUser_UserId").param("userId","999"))
                .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.holdings").isEmpty());
    }

    @Test
    void testGetById_Positive() throws Exception{
        VirtualGoldHoldings h = new VirtualGoldHoldings();
        h.setQuantity(BigDecimal.valueOf(5));
        h.setCreatedAt(LocalDateTime.now());
        h = repository.save(h);

        mockMvc.perform(get("/virtual_gold_holdings/" + h.getHoldingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void testGetById_NotFound() throws Exception{

        mockMvc.perform(get("/virtual_gold_holdings/999"))
                .andExpect(status().isBadRequest());
    }

}