package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException{
    public ForbiddenException(String message){
        super(message, HttpStatus.FORBIDDEN);
    }
}
