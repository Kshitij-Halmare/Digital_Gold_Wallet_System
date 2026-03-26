package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class VirtualGoldHoldingsRepoTest {

    @Autowired
    private VirtualGoldHoldingsRepo repository;

    private VirtualGoldHoldings h1, h2;

    @BeforeEach
    void setUp() {
        h1 = new VirtualGoldHoldings();
        h1.setQuantity(new BigDecimal("10.5"));
        h1.setCreatedAt(LocalDateTime.now());

        h2 = new VirtualGoldHoldings();
        h2.setQuantity(new BigDecimal("5.0"));
        h2.setCreatedAt(LocalDateTime.now());

        repository.saveAll(List.of(h1, h2));
    }

    @Test
    void testFindAll() {
        List<VirtualGoldHoldings> list = repository.findAll();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testSave() {
        VirtualGoldHoldings h = new VirtualGoldHoldings();
        h.setQuantity(new BigDecimal("7.5"));
        h.setCreatedAt(LocalDateTime.now());

        VirtualGoldHoldings saved = repository.save(h);

        assertNotNull(saved.getHoldingId());
        assertEquals(new BigDecimal("7.5"), saved.getQuantity());
    }
}
