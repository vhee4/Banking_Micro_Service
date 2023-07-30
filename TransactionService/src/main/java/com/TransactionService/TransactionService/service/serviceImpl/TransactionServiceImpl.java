package com.TransactionService.TransactionService.service.serviceImpl;

import com.TransactionService.TransactionService.dto.*;
import com.TransactionService.TransactionService.entity.Transaction;
import com.TransactionService.TransactionService.entity.TransactionType;
import com.TransactionService.TransactionService.repository.TransactionRepository;
import com.TransactionService.TransactionService.service.serviceInterface.TransactionService;
import com.TransactionService.TransactionService.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;


    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction newTransaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(newTransaction);

    }

    @Cacheable(cacheNames = "fetchAllTransactions")
    public List<TransactionDto> fetchAllTransactionsByUser(String accountNumber) {
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionList.stream().map(list -> TransactionDto.builder()
                .transactionType(list.getTransactionType())
                .accountNumber(list.getAccountNumber())
                .amount(list.getAmount())
//                        .transactionDate(list.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

//    public List<TransactionDto> getAllTransactionsByDate(String date){
//        List<Transaction> transactionList = transactionRepository.findByCreatedAt(date);
//        return transactionList.stream().map((transaction -> mappedToTransferResponse(transaction))).collect(Collectors.toList());
//    }

//    @Override
//    @Cacheable(cacheNames = "filterByAmount")
//    public List<TransactionDto> filterByAmount(double minAmount, double maxAmount) {
////        System.out.println("fetching records from cache");
//        List<Transaction> transactionList = getRecordFromDB(minAmount,maxAmount);
//        return transactionList.stream().map((transaction -> mappedToTransferResponse(transaction))).collect(Collectors.toList());
//    }

//    private List<Transaction> getRecordFromDB(double minAmount, double maxAmount) {
//        System.out.println("fetching records from DB");
//        return transactionRepository.findByAmountBetween(minAmount, maxAmount);
//    }


//    private TransactionDto mappedToTransferResponse(Transaction transaction) {
//        return TransactionDto.builder()
//                .amount(transaction.getAmount())
//                .transactionDate(transaction.getCreatedAt())
//                .accountNumber(transaction.getAccountNumber())
//                .transactionType(transaction.getTransactionType())
//                .build();
//    }

    public UserResponse credit(TransactionRequest transactionRequest) {
        UserData user = retrieveUser(transactionRequest.getAccountNumber());
        if (user == null) {
            return UserResponse.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
        }
        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.CREDIT)
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();
        saveTransaction(transactionDto);

        TransactionRequest transactionRequest1 = TransactionRequest.builder()
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();
        UserResponse response = creditAndUpdateUserBalance(transactionRequest1);
        if (response == null) {
            return UserResponse.builder()
                    .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION_CODE)
                    .responseMessage(ResponseUtils.UNSUCCESSFUL_TRANSACTION_CODE)
                    .userData(null)
                    .build();
        }
        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .userData(UserData.builder()
                        .accountName(user.getAccountName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }


    @Override
    public UserResponse debit(TransactionRequest transactionRequest) {
        UserData user = retrieveUser(transactionRequest.getAccountNumber());
        if (user == null) {
            return UserResponse.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
        }
        if (transactionRequest.getAmount().compareTo(user.getAccountBalance()) > 0) {
            return UserResponse.builder()
                    .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION_CODE)
                    .responseMessage(ResponseUtils.INSUFFICIENT_FUNDS)
                    .userData(null)
                    .build();
        }
        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.DEBIT);
        transactionDto.setAccountNumber(user.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());
        saveTransaction(transactionDto);

        TransactionRequest transactionRequest1 = TransactionRequest.builder()
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();
        debitAndUpdateUserBalance(transactionRequest1);
        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                .userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(ResponseUtils.truncatedAccountNumber(transactionRequest.getAccountNumber()))
                        .accountName(user.getAccountName())
                        .build())

                .build();
    }

    @Override
    public TransferResponse transfer(TransferRequest transferRequest){
        UserData sender = retrieveUser(transferRequest.getSenderAccountNumber());
        UserData recipient = retrieveUser(transferRequest.getRecipientAccountNumber());
        if(sender==null || recipient==null){
            return TransferResponse.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .transferData(null)
                    .build();
        }
        if(transferRequest.getAmount().compareTo(sender.getAccountBalance()) > 0){
            return TransferResponse.builder()
                    .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION_CODE)
                    .responseMessage(ResponseUtils.INSUFFICIENT_FUNDS)
                    .transferData(null)
                    .build();
        }

        TransactionRequest creditRequest = new TransactionRequest(recipient.getAccountNumber(), transferRequest.getAmount());
        TransactionRequest debitRequest = new TransactionRequest(sender.getAccountNumber(), transferRequest.getAmount());

        UserResponse debitResponse = debit(debitRequest);
        if(!debitResponse.getResponseCode().equalsIgnoreCase(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)){
            return TransferResponse.builder()
                    .responseCode(ResponseUtils.UNSUCCESSFUL_TRANSACTION_CODE)
                    .responseMessage("Unsuccessful Transaction")
                    .transferData(null)
                    .build();
        }
        credit(creditRequest);

        return TransferResponse.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                .transferData(TransferData.builder()
                        .SendersAccountNumber(sender.getAccountNumber())
                        .SendersAccountName(sender.getAccountName())
                        .RecipientsAccountNumber(recipient.getAccountNumber())
                        .amountTransferred(transferRequest.getAmount())
                        .RecipientsAccountName(recipient.getAccountName())
                        .SendersAccountBalance(debitResponse.getUserData().getAccountBalance())
                        .build())
                .build();
    }

    private UserData retrieveUser(String accountNumber) {
        UserResponse response = webClient.get()
                .uri("http://localhost:8080/api/users/balEnquiry",
                        uriBuilder -> uriBuilder.queryParam("accountNumber", accountNumber).build())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        assert response != null;
        return UserData.builder()
                .accountName(response.getUserData().getAccountName())
                .accountBalance(response.getUserData().getAccountBalance())
                .accountNumber(response.getUserData().getAccountNumber())
                .build();
    }

    private UserResponse creditAndUpdateUserBalance(TransactionRequest transactionRequest) {
        return webClient.post()
                .uri("http://localhost:8080/api/users/credit-and-update-accountBalance")
                .body(BodyInserters.fromValue(transactionRequest))
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    private UserResponse debitAndUpdateUserBalance(TransactionRequest transactionRequest) {
        return webClient.post()
                .uri("http://localhost:8080/api/users/debit-and-update-accountBalance")
                .body(BodyInserters.fromValue(transactionRequest))
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

}
