package com.example.Digital_Gold_Wallet_System.enums;

public enum TransactionType {

    CREDITED("Credited to wallet"),
    DEBITED("Debited from wallet");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
