package com.example.Digital_Gold_Wallet_System.Projection;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "addressView",types = Addresses.class)
public interface AddressProjection {
    String getStreet();
    String getCity();
    String getState();
    String getPostalCode();
    String getCountry();
}