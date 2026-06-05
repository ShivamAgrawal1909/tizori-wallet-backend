package com.ewallet.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Transaction save karo
    @PostMapping("/save")
    public ResponseEntity<Transaction> saveTransaction(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam Double amount,
            @RequestParam String type) {
        Transaction t = transactionService.saveTransaction(
                fromUserId, toUserId, amount, type, "SUCCESS");
        return ResponseEntity.ok(t);
    }

    // Mere saare transactions dekho
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getMyTransactions(userId));
    }
}