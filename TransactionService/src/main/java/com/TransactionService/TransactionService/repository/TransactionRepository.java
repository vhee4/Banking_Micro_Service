package com.TransactionService.TransactionService.repository;

import com.TransactionService.TransactionService.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
//    List<Transaction> findByCreatedAt(String date);
    List<Transaction> findByAmountBetween(double minAmount, double maxAmount);

}
