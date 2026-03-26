package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "users")
@CrossOrigin(origins = "*")
public interface UsersRepo extends JpaRepository<Users, Integer> {

    List<Users> findByNameContainingIgnoreCase(String name);

    List<Users> findByEmailContainingIgnoreCase(String email);

}
