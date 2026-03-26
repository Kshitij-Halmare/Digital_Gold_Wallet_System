package com.example.Digital_Gold_Wallet_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Digital_Gold_Wallet_System.dto.request.UsersRequestDTO;
import com.example.Digital_Gold_Wallet_System.dto.response.UsersResponseDTO;
import com.example.Digital_Gold_Wallet_System.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService userService;

    @PostMapping("/add")
    public ResponseEntity<UsersResponseDTO> addUser(@RequestBody UsersRequestDTO userDTO) {

       return new ResponseEntity<>(userService.addUser(userDTO),HttpStatus.CREATED);
    }
}
