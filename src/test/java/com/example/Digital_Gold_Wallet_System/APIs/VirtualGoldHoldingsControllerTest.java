package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.entity.VendorBranches;
import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import com.example.Digital_Gold_Wallet_System.repository.VendorBranchesRepo;
import com.example.Digital_Gold_Wallet_System.repository.VirtualGoldHoldingsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private VendorBranchesRepo branchRepo;

    @Autowired
    private UsersRepo userRepo;

    @Test
    void testGetByUserAndBranch_Positive() throws Exception {


        Users user = new Users();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepo.save(user);


        VendorBranches branch = new VendorBranches();
        branch.setQuantity(BigDecimal.valueOf(100));
        branch = branchRepo.save(branch);

        VirtualGoldHoldings h = new VirtualGoldHoldings();
        h.setUser(user);
        h.setBranch(branch);
        h.setQuantity(BigDecimal.valueOf(10));
        h.setCreatedAt(LocalDateTime.now());

        repository.save(h);

        // ✅ Step 4: Call API
        mockMvc.perform(get("/virtual_gold_holdings/search/findByUser_UserIdAndBranch_BranchId")
                        .param("userId", user.getUserId().toString())
                        .param("branchId", branch.getBranchId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.holdings").exists());
    }

    @Test
    void testGetByUserAndBranch_NotFound() throws Exception {


        mockMvc.perform(get("/virtual_gold_holdings/search/findByUser_UserIdAndBranch_BranchId")
                        .param("userId", "1")
                        .param("branchId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.holdings").isEmpty());
    }

    @Test
    void testAddHolding_Positive() throws Exception {

        String json = """
    {
        "quantity": 15,
        "createdAt": "2026-03-27T10:00:00"
    }
    """;

        mockMvc.perform(post("/virtual_gold_holdings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddHolding_Invalid() throws Exception {

        String json = """
    {
        "quantity": "invalid"
    }
    """;

        mockMvc.perform(post("/virtual_gold_holdings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}