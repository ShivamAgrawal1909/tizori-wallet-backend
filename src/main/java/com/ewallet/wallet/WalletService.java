package com.ewallet.wallet;

import com.ewallet.config.KafkaProducer;
import com.ewallet.transaction.Transaction;
import com.ewallet.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private static final String WALLET_CACHE_PREFIX = "wallet:";

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(0.0);

        Wallet savedWallet = walletRepository.save(wallet);

        redisTemplate.opsForValue()
                .set(WALLET_CACHE_PREFIX + userId, savedWallet);

        kafkaProducer.sendWalletEvent(
                "WALLET_CREATED | userId=" + userId
        );

        return savedWallet;
    }

    public Wallet getWallet(Long userId) {

        String key = WALLET_CACHE_PREFIX + userId;

        Object cachedWallet = redisTemplate.opsForValue().get(key);

        if (cachedWallet instanceof Wallet) {
            System.out.println("Wallet fetched from Redis cache");
            return (Wallet) cachedWallet;
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));

        redisTemplate.opsForValue().set(key, wallet);

        System.out.println("Wallet fetched from MySQL and saved to Redis");

        return wallet;
    }

    public Wallet addMoney(Long userId, Double amount) {

        Wallet wallet = getWallet(userId);

        wallet.setBalance(wallet.getBalance() + amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        redisTemplate.opsForValue()
                .set(WALLET_CACHE_PREFIX + userId, updatedWallet);

        Transaction tx = new Transaction();
        tx.setToUserId(userId);
        tx.setAmount(amount);
        tx.setType("ADD_MONEY");
        tx.setStatus("SUCCESS");

        transactionRepository.save(tx);

        kafkaProducer.sendWalletEvent(
                "MONEY_ADDED | userId=" + userId +
                        " | amount=" + amount +
                        " | balance=" + updatedWallet.getBalance()
        );

        return updatedWallet;
    }

    public String transferMoney(Long fromUserId, Long toUserId, Double amount) {

        Wallet fromWallet = getWallet(fromUserId);
        Wallet toWallet = getWallet(toUserId);

        if (fromWallet.getBalance() < amount) {

            kafkaProducer.sendWalletEvent(
                    "TRANSFER_FAILED | fromUserId=" + fromUserId +
                            " | toUserId=" + toUserId +
                            " | amount=" + amount +
                            " | reason=INSUFFICIENT_BALANCE"
            );

            throw new RuntimeException("Insufficient balance!");
        }

        fromWallet.setBalance(fromWallet.getBalance() - amount);
        toWallet.setBalance(toWallet.getBalance() + amount);

        Wallet updatedFromWallet = walletRepository.save(fromWallet);
        Wallet updatedToWallet = walletRepository.save(toWallet);

        redisTemplate.opsForValue()
                .set(WALLET_CACHE_PREFIX + fromUserId, updatedFromWallet);

        redisTemplate.opsForValue()
                .set(WALLET_CACHE_PREFIX + toUserId, updatedToWallet);

        Transaction tx = new Transaction();
        tx.setFromUserId(fromUserId);
        tx.setToUserId(toUserId);
        tx.setAmount(amount);
        tx.setType("TRANSFER");
        tx.setStatus("SUCCESS");

        transactionRepository.save(tx);

        kafkaProducer.sendWalletEvent(
                "TRANSFER_SUCCESS | fromUserId=" + fromUserId +
                        " | toUserId=" + toUserId +
                        " | amount=" + amount +
                        " | fromBalance=" + updatedFromWallet.getBalance() +
                        " | toBalance=" + updatedToWallet.getBalance()
        );

        return "Transfer successful!";
    }
}