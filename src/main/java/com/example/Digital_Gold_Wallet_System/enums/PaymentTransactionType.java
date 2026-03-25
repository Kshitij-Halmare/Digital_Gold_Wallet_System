package com.example.Digital_Gold_Wallet_System.enums;

public enum PaymentTransactionType {

    CREDITED("Credited to wallet"),
    DEBITED("Debited from wallet");

    private final String value;

    PaymentTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}