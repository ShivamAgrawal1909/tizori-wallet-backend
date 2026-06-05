package com.ewallet.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Transaction save karo
    public Transaction saveTransaction(Long fromUserId, Long toUserId,
                                       Double amount, String type, String status) {
        Transaction transaction = new Transaction();
        transaction.setFromUserId(fromUserId);
        transaction.setToUserId(toUserId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }

    // Mere saare transactions dekho
    public List<Transaction> getMyTransactions(Long userId) {
        List<Transaction> sent = transactionRepository.findByFromUserId(userId);
        List<Transaction> received = transactionRepository.findByToUserId(userId);
        sent.addAll(received);
        return sent;
    }
}