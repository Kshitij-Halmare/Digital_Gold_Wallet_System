package com.example.Digital_Gold_Wallet_System.entity;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String email;

    private String name;

    // Big Decimal handles floating point issues better than Double.
    private BigDecimal balance; 

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

=======
public class Users {

>>>>>>> 14f0dd6b1796064b9098d424ac5335349a7a2b53
}
