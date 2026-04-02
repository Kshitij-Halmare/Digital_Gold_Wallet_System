package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException{
    public ResourceNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
