package com.example.Digital_Gold_Wallet_System.common.pagination;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtility {

    public Pageable createPageable(PaginationRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(request.getSortBy()).descending() :
                Sort.by(request.getSortBy()).ascending();

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    public <T> PaginationResponse<T> toPaginationResponse(Page<T> page) {
        return PaginationResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
