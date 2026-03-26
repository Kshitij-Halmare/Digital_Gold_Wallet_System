package com.example.Digital_Gold_Wallet_System.entity.enums;

public enum PaymentMethod {

    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    GOOGLE_PAY("Google Pay"),
    AMAZON_PAY("Amazon Pay"),
    PHONEPE("PhonePe"),
    PAYTM("Paytm"),
    UPI("UPI"),
    BANK_TRANSFER("Bank Transfer");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
