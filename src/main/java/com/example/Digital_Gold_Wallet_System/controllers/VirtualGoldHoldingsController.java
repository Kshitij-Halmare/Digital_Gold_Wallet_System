package com.example.Digital_Gold_Wallet_System.controllers;

import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.service.VirtualGoldHoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/virtualGoldHoldings")
public class VirtualGoldHoldingsController {

    @Autowired
    VirtualGoldHoldingsService virtualGoldHoldingsService;

    @PostMapping("/sell")
    public String sellVirtualGold(@RequestParam Integer holdingId, @RequestParam PaymentMethod pMethod){
        virtualGoldHoldingsService.sellVirtualGoldHoldings(holdingId, pMethod);
        return "success";
    }

    @PostMapping("/buy")
    public String buyVirtualGold(@RequestParam BigDecimal qty, @RequestParam Integer userId, @RequestParam Integer branchId, @RequestParam PaymentMethod pMethod){
        virtualGoldHoldingsService.buyVirtualGoldHoldings(userId, qty, branchId, pMethod);
        return "success";
    }

    @PostMapping("/convert/{id}")
    public String convert(@PathVariable Integer id) {

        try {
            virtualGoldHoldingsService.convertToPhysical(id);
            return "Converted successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

}
