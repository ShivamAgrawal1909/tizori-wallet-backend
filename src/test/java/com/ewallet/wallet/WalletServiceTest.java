package com.ewallet.wallet;

import com.ewallet.config.KafkaProducer;
import com.ewallet.transaction.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private WalletService walletService;

    @Test
    void addMoney_ShouldIncreaseBalance() {

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(100.0);

        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);

        when(valueOperations.get("wallet:1"))
                .thenReturn(null);

        when(walletRepository.findByUserId(1L))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.addMoney(1L, 50.0);

        assertEquals(150.0, result.getBalance());

        verify(walletRepository, times(1))
                .save(any(Wallet.class));

        verify(transactionRepository, times(1))
                .save(any());

        verify(kafkaProducer, times(1))
                .sendWalletEvent(contains("MONEY_ADDED"));
    }
}