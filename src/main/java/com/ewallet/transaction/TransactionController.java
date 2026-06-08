package com.ewallet.transaction;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    // User ke saare transactions
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                transactionService.getMyTransactions(userId)
        );
    }

    // Saare transactions
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {

        return ResponseEntity.ok(
                transactionRepository.findAll()
        );
    }

    // Dashboard Summary
    @GetMapping("/summary/{userId}")
    public ResponseEntity<TransactionSummary> getSummary(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                transactionService.getSummary(userId)
        );
    }
}