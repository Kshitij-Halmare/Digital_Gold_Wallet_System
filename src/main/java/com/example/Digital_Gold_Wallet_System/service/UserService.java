package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.exception.ResourceNotFoundException;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UsersRepo usersRepo;

    public String depositMoney(Integer userId, BigDecimal amount){
        Users user = usersRepo.findById(userId).get();
        user.setBalance(user.getBalance().add(amount));
        return usersRepo.save(user).getBalance().toString();
    }

}
