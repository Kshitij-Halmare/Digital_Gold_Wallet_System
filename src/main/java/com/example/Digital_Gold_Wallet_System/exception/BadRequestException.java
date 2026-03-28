package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException{
    public BadRequestException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
