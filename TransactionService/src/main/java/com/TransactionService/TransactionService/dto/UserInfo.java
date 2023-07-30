package com.TransactionService.TransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
public class UserInfo {
        private String accountNumber;
        private String accountName;
        private BigDecimal accountBalance;
}
