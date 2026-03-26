package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.VirtualGoldHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "virtual_gold_holdings",
        collectionResourceRel = "holdings"
)
public interface VirtualGoldHoldingsRepo extends JpaRepository<VirtualGoldHoldings, Integer> {

}
