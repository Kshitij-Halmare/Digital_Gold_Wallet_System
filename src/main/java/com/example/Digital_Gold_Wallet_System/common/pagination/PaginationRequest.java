package com.example.Digital_Gold_Wallet_System.common.pagination;

import lombok.Data;

@Data
public class PaginationRequest {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}