package com.example.Digital_Gold_Wallet_System.Controllers;

import com.example.Digital_Gold_Wallet_System.service.VirtualGoldHoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/virtualGoldHoldings")
public class VirtualGoldHoldingsController {
    @Autowired
    private VirtualGoldHoldingsService service;
    @PostMapping("/convert/{id}")
    public String convert(@PathVariable Integer id) {

        try {
            service.convertToPhysical(id);
            return "Converted successfully";
        } catch (RuntimeException e) {
            return "ResponseEntity.badRequest().body(e.getMessage())";
        }
    }
}
