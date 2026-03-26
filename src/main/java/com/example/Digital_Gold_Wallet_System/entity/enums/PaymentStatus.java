package com.example.Digital_Gold_Wallet_System.entity.enums;

public enum PaymentStatus {

    SUCCESS("Success"),
    FAILED("Failed");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
