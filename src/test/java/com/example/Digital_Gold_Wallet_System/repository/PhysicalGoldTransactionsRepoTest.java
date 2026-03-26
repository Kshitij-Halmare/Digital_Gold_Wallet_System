package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.PhysicalGoldTransactions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest

public class PhysicalGoldTransactionsRepoTest {


    @Autowired
    PhysicalGoldTransactionsRepo physicalGoldTransactionsRepo;


    @AfterEach
    void cleanup(){
        physicalGoldTransactionsRepo.deleteAll();
    }


    @Test
    void testSave(){
        //positive scenario for saving


        PhysicalGoldTransactions physicalGoldTransactions=new PhysicalGoldTransactions();
        physicalGoldTransactions.setQuantity(BigDecimal.valueOf(300));
        physicalGoldTransactions.setCreatedAt(LocalDateTime.now());


        PhysicalGoldTransactions saved=physicalGoldTransactionsRepo.save(physicalGoldTransactions);


        assertThat(saved.getTransactionId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualByComparingTo(physicalGoldTransactions.getQuantity());


    }


    @Test
    void testFindById(){
        //positive scenario for find by id


        PhysicalGoldTransactions physicalGoldTransactions=new PhysicalGoldTransactions();
        physicalGoldTransactions.setQuantity(BigDecimal.valueOf(200));
        physicalGoldTransactions.setCreatedAt(LocalDateTime.now());


        PhysicalGoldTransactions saved=physicalGoldTransactionsRepo.save(physicalGoldTransactions);


        Optional<PhysicalGoldTransactions> fetched=physicalGoldTransactionsRepo.findById(saved.getTransactionId());


        assertThat(fetched).isPresent();
        assertThat(fetched.get().getQuantity()).isEqualByComparingTo(physicalGoldTransactions.getQuantity());




    }


    @Test
    void testInvalidIdForFindById(){
        //negative scenario for find by id


        PhysicalGoldTransactions physicalGoldTransactions=new PhysicalGoldTransactions();
        physicalGoldTransactions.setQuantity(BigDecimal.valueOf(200));
        physicalGoldTransactions.setCreatedAt(LocalDateTime.now());


        PhysicalGoldTransactions saved=physicalGoldTransactionsRepo.save(physicalGoldTransactions);


        //creation of invalid id
        int invalidId=saved.getTransactionId()+1000;


        Optional<PhysicalGoldTransactions> fetched=physicalGoldTransactionsRepo.findById(invalidId);


        assertThat(fetched).isEmpty();


    }


}

