package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException{
    public UnauthorizedException(String message){
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
