package com.TransactionService.TransactionService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferData {
    private String SendersAccountName;
    private String SendersAccountNumber;
    private BigDecimal SendersAccountBalance;
    private String RecipientsAccountNumber;
    private String RecipientsAccountName;
    private BigDecimal amountTransferred;
}
