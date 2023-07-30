package com.TransactionService.TransactionService.service.serviceInterface;


import com.TransactionService.TransactionService.dto.*;

import java.util.List;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);

    List<TransactionDto> fetchAllTransactionsByUser(String accountNumber);

    UserResponse debit(TransactionRequest transactionRequest);

    UserResponse credit(TransactionRequest transactionRequest);

    TransferResponse transfer(TransferRequest transferRequest);

//    List<TransactionDto> getAllTransactionsByDate(String date);


//    List<TransactionDto> filterByAmount(double minAmount, double maxAmount);
}
