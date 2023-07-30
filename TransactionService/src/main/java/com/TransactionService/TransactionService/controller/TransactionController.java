package com.TransactionService.TransactionService.controller;

import com.TransactionService.TransactionService.dto.TransactionRequest;
import com.TransactionService.TransactionService.dto.UserResponse;
import com.TransactionService.TransactionService.service.serviceInterface.TransactionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transaction")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PutMapping("/debit")
    public ResponseEntity<UserResponse> debitRequest(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.debit(transactionRequest), HttpStatus.OK);
    }
    @PutMapping("/credit")
    public ResponseEntity<UserResponse> creditRequest(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.credit(transactionRequest), HttpStatus.OK);
    }
//    @GetMapping("/filterByAmount")
//    public List<TransactionDto> filterByAmount(@RequestParam(name = "minAmount") double minAmount, @RequestParam(name = "maxAmount") double maxAmount){
//        return transactionService.filterByAmount(minAmount,maxAmount);
//    }


}
