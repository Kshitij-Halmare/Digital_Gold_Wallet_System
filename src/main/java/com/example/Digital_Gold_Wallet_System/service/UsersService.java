package com.example.Digital_Gold_Wallet_System.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Digital_Gold_Wallet_System.dto.request.UsersRequestDTO;
import com.example.Digital_Gold_Wallet_System.dto.response.UsersResponseDTO;
import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;

@Service
public class UsersService {

    @Autowired
    UsersRepo userDAO;

    @Autowired
    ModelMapper mapper;

    public UsersResponseDTO addUser(UsersRequestDTO usersDTO){
        Users user = mapper.map(usersDTO, Users.class);
        user.setBalance(new BigDecimal(0));
        user.setCreatedAt(LocalDateTime.now());

        //save user
        Users savedUser = userDAO.save(user);

        return mapper.map(savedUser, UsersResponseDTO.class);
    }

}
