package com.TransactionService.TransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private String transactionType;
    private String transferFrom;
    private String transferTo;
    private BigDecimal amount;
}
