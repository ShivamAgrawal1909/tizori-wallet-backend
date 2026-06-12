package com.ewallet.transaction;

import com.ewallet.user.User;
import com.ewallet.user.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

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

    // User transactions with sender/receiver details
    public List<TransactionResponse> getMyTransactionResponses(Long userId) {

        List<Transaction> transactions = getMyTransactions(userId);

        return transactions.stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());

        response.setFromUserId(transaction.getFromUserId());
        response.setToUserId(transaction.getToUserId());

        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());

        if (transaction.getFromUserId() != null) {
            User sender = userRepository.findById(transaction.getFromUserId())
                    .orElse(null);

            if (sender != null) {
                response.setSenderName(sender.getName());
                response.setSenderEmail(sender.getEmail());
            }
        }

        if (transaction.getToUserId() != null) {
            User receiver = userRepository.findById(transaction.getToUserId())
                    .orElse(null);

            if (receiver != null) {
                response.setReceiverName(receiver.getName());
                response.setReceiverEmail(receiver.getEmail());
            }
        }

        return response;
    }

    // FAST & OPTIMIZED: Dashboard summary using database-level aggregations
    public TransactionSummary getSummary(Long userId) {

        TransactionSummary summary = new TransactionSummary();

        // 1. Database se seedhe calculation ho rhi hai (Memory load nahi hogi)
        double totalAdded = transactionRepository.sumTotalAddedByUserId(userId);
        double totalSent = transactionRepository.sumTotalSentByUserId(userId);
        double totalReceived = transactionRepository.sumTotalReceivedByUserId(userId);
        long totalTransactions = transactionRepository.countTotalTransactionsByUserId(userId);

        double currentBalance = walletRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Wallet not found"))
                .getBalance();

        summary.setCurrentBalance(currentBalance);
        summary.setTotalAdded(totalAdded);
        summary.setTotalSent(totalSent);
        summary.setTotalReceived(totalReceived);
        summary.setTotalTransactions(totalTransactions);

        return summary;
    }
}