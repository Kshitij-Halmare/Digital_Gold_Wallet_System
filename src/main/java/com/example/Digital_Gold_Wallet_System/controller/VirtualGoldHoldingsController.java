package com.example.Digital_Gold_Wallet_System.controller;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import com.example.Digital_Gold_Wallet_System.repository.VirtualGoldHoldingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/virtual_gold_holding")
public class VirtualGoldHoldingsController {

    @Autowired
    private VirtualGoldHoldingsRepo repository;

    @GetMapping
    public ResponseEntity<List<VirtualGoldHoldings>> getAllHoldings() {
        return ResponseEntity.ok(repository.findAll());
    }
}
