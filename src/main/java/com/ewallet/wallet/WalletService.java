package com.ewallet.wallet;

import com.ewallet.config.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    // Wallet create karo
    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(0.0);
        return walletRepository.save(wallet);
    }

    // Balance check karo
    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));
    }

    // Paisa add karo
    public Wallet addMoney(Long userId, Double amount) {
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance() + amount);
        kafkaProducer.sendMessage("wallet-transactions",
                "Money added: " + amount + " to userId: " + userId);
        return walletRepository.save(wallet);
    }

    // Paisa transfer karo
    public String transferMoney(Long fromUserId, Long toUserId, Double amount) {
        Wallet fromWallet = getWallet(fromUserId);
        Wallet toWallet = getWallet(toUserId);

        if (fromWallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance!");
        }

        fromWallet.setBalance(fromWallet.getBalance() - amount);
        toWallet.setBalance(toWallet.getBalance() + amount);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // Kafka message bhejo
        kafkaProducer.sendMessage("wallet-transactions",
                "Transfer: " + amount + " from userId: " + fromUserId + " to userId: " + toUserId);

        return "Transfer successful!";
    }
}