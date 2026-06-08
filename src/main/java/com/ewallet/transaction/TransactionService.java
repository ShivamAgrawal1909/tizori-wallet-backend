package com.ewallet.transaction;

import com.ewallet.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    // Transaction save
    public Transaction saveTransaction(Long fromUserId,
                                       Long toUserId,
                                       Double amount,
                                       String type,
                                       String status) {

        Transaction transaction = new Transaction();

        transaction.setFromUserId(fromUserId);
        transaction.setToUserId(toUserId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);

        return transactionRepository.save(transaction);
    }

    // User transactions
    public List<Transaction> getMyTransactions(Long userId) {

        List<Transaction> sent =
                transactionRepository.findByFromUserId(userId);

        List<Transaction> received =
                transactionRepository.findByToUserId(userId);

        sent.addAll(received);

        return sent;
    }

    // Summary
    public TransactionSummary getSummary(Long userId) {

        TransactionSummary summary = new TransactionSummary();

        double totalAdded = transactionRepository.findAll()
                .stream()
                .filter(t ->
                        "ADD_MONEY".equals(t.getType())
                                && userId.equals(t.getToUserId()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalSent = transactionRepository.findAll()
                .stream()
                .filter(t ->
                        "TRANSFER".equals(t.getType())
                                && userId.equals(t.getFromUserId()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalReceived = transactionRepository.findAll()
                .stream()
                .filter(t ->
                        "TRANSFER".equals(t.getType())
                                && userId.equals(t.getToUserId()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double currentBalance = walletRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Wallet not found"))
                .getBalance();

        summary.setCurrentBalance(currentBalance);
        summary.setTotalAdded(totalAdded);
        summary.setTotalSent(totalSent);
        summary.setTotalReceived(totalReceived);

        return summary;
    }
}