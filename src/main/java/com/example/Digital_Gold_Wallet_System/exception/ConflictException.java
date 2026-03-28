package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends AppException{
    public ConflictException(String message){
        super(message, HttpStatus.CONFLICT);
    }
}
