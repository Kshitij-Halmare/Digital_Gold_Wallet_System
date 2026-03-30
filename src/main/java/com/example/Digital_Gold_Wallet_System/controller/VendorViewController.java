package com.example.Digital_Gold_Wallet_System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorViewController {
    @GetMapping("/vendors-page")
    public String vendorsPage() {
        return "vendors"; // → templates/vendors.html
    }
}
