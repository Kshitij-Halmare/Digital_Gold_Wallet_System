package com.example.Digital_Gold_Wallet_System.Controllers;

import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.service.VirtualGoldHoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
