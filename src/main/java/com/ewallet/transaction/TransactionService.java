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

    // Dashboard summary
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
                        userId.equals(t.getToUserId())
                                && (
                                "TRANSFER".equals(t.getType())
                                        || "ADD_MONEY".equals(t.getType())
                        ))
                .mapToDouble(Transaction::getAmount)
                .sum();

        long totalTransactions = transactionRepository.findAll()
                .stream()
                .filter(t ->
                        userId.equals(t.getFromUserId())
                                || userId.equals(t.getToUserId()))
                .count();

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