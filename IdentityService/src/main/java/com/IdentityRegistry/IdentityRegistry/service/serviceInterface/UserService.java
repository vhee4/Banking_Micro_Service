package com.IdentityRegistry.IdentityRegistry.service.serviceInterface;

import com.IdentityRegistry.IdentityRegistry.dto.TransactionRequest;
import com.IdentityRegistry.IdentityRegistry.dto.UserData;
import com.IdentityRegistry.IdentityRegistry.dto.UserResponse;

import java.util.List;

public interface UserService {


    List<UserResponse> allUsers();

    UserResponse fetchUser(Long userId);

    UserResponse balanceEnquiry(String accountNumber);

    UserResponse nameEnquiry(String accountNumber);

    UserResponse deleteUser(String accountNumber);

    UserResponse UpdateUserBalanceAfterCredit(TransactionRequest transactionRequest);

    UserResponse UpdateUserBalanceAfterDebit(TransactionRequest transactionRequest);

//
//    UserResponse transfer(TransferRequest transferRequest);

//    List<TransactionDto> fetchAllTransactionsByUser(String accountNumber);
//    List<TransactionDto> getAllTransactionsByDate(String date);


//    List<TransactionDto> fetchTransactionsByDate(String date);

}
