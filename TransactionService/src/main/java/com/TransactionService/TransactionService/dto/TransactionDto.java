package com.TransactionService.TransactionService.dto;

import com.TransactionService.TransactionService.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private TransactionType transactionType;
    private String accountNumber;
    private BigDecimal amount;
//    private LocalDate transactionDate;
}
