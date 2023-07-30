package com.TransactionService.TransactionService.service.serviceInterface;



import com.TransactionService.TransactionService.dto.TransactionDto;
import com.TransactionService.TransactionService.dto.TransactionRequest;
import com.TransactionService.TransactionService.dto.UserResponse;

import java.util.List;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
    List<TransactionDto> fetchAllTransactionsByUser(String accountNumber);
    public UserResponse debit(TransactionRequest transactionRequest);
    public UserResponse credit(TransactionRequest transactionRequest);

//    List<TransactionDto> getAllTransactionsByDate(String date);


//    List<TransactionDto> filterByAmount(double minAmount, double maxAmount);
}
