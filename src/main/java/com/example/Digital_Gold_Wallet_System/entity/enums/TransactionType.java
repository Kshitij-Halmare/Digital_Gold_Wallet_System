package com.example.Digital_Gold_Wallet_System.entity.enums;

public enum TransactionType {

    BUY("Buy"),
    SELL("Sell"),
    CONVERT_TO_PHYSICAL("Convert to Physical");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
