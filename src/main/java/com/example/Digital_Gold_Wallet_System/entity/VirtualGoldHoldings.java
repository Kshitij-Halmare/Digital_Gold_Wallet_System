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
public class VirtualGoldHoldings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holdingId;

    private BigDecimal quantity;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

=======
public class VirtualGoldHoldings {

>>>>>>> 14f0dd6b1796064b9098d424ac5335349a7a2b53
}
