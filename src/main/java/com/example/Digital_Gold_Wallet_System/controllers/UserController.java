package com.example.Digital_Gold_Wallet_System.controllers;

import com.example.Digital_Gold_Wallet_System.Projection.PhysicalGoldTransactionProjection;
import com.example.Digital_Gold_Wallet_System.Projection.UserListProjection;
import com.example.Digital_Gold_Wallet_System.Projection.UsersProjection;
import com.example.Digital_Gold_Wallet_System.entity.enums.TransactionType;
import com.example.Digital_Gold_Wallet_System.repository.TransactionHistoryRepo;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import com.example.Digital_Gold_Wallet_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionHistoryRepo  transactionHistoryRepo;

    @Autowired
    private UsersRepo usersRepo;

    @GetMapping("/summary")
    public List<UsersProjection> getUsersSummary(){
        return usersRepo.findAllProjectedBy();
    }

    @PutMapping("/deposit/{userId}")
    public String depositMoney(@PathVariable Integer userId, @RequestParam BigDecimal amount){
        return userService.depositMoney(userId, amount);
    }

    @GetMapping("/physical/{userId}")
    public List<PhysicalGoldTransactionProjection> getPhysicalTransactions(@PathVariable Integer userId) {

        return transactionHistoryRepo
                .findByUserUserIdAndTransactionType(userId, TransactionType.CONVERT_TO_PHYSICAL);
    }
}
