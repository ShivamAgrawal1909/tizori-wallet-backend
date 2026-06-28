package com.ewallet.transaction;

import com.ewallet.wallet.Wallet;
import com.ewallet.wallet.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getSummary_ShouldReturnCorrectData() {

        Long userId = 1L;

        
        when(transactionRepository.sumTotalAddedByUserId(userId))
                .thenReturn(100.0);

        when(transactionRepository.sumTotalSentByUserId(userId))
                .thenReturn(30.0);

        when(transactionRepository.sumTotalReceivedByUserId(userId))
                .thenReturn(20.0);

        when(transactionRepository.countTotalTransactionsByUserId(userId))
                .thenReturn(3L);

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(90.0);

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(wallet));

    
        TransactionSummary summary = transactionService.getSummary(userId);

        
        assertEquals(100.0, summary.getTotalAdded());
        assertEquals(30.0,  summary.getTotalSent());
        assertEquals(20.0,  summary.getTotalReceived());
        assertEquals(3L,    summary.getTotalTransactions());
        assertEquals(90.0,  summary.getCurrentBalance());
    }
}
