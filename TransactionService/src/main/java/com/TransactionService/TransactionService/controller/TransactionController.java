package com.TransactionService.TransactionService.controller;

import com.TransactionService.TransactionService.dto.TransactionRequest;
import com.TransactionService.TransactionService.dto.TransferRequest;
import com.TransactionService.TransactionService.dto.TransferResponse;
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
    public ResponseEntity<UserResponse> debit(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.debit(transactionRequest), HttpStatus.OK);
    }
    @PutMapping("/credit")
    public ResponseEntity<UserResponse> credit(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.credit(transactionRequest), HttpStatus.OK);
    }
    @PutMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(transactionService.transfer(transferRequest), HttpStatus.OK);
    }
//    @GetMapping("/filterByAmount")
//    public List<TransactionDto> filterByAmount(@RequestParam(name = "minAmount") double minAmount, @RequestParam(name = "maxAmount") double maxAmount){
//        return transactionService.filterByAmount(minAmount,maxAmount);
//    }


}
