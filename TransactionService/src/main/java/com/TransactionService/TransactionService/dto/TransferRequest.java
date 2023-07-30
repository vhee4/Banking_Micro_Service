package com.TransactionService.TransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private BigDecimal amount;
}
