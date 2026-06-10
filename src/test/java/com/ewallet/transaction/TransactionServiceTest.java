package com.ewallet.transaction;

import com.ewallet.wallet.Wallet;
import com.ewallet.wallet.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

        Transaction addMoney = new Transaction();
        addMoney.setType("ADD_MONEY");
        addMoney.setToUserId(1L);
        addMoney.setAmount(100.0);

        Transaction transferSent = new Transaction();
        transferSent.setType("TRANSFER");
        transferSent.setFromUserId(1L);
        transferSent.setAmount(30.0);

        Transaction transferReceived = new Transaction();
        transferReceived.setType("TRANSFER");
        transferReceived.setToUserId(1L);
        transferReceived.setAmount(20.0);

        when(transactionRepository.findAll())
                .thenReturn(List.of(
                        addMoney,
                        transferSent,
                        transferReceived
                ));

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(90.0);

        when(walletRepository.findByUserId(1L))
                .thenReturn(Optional.of(wallet));

        TransactionSummary summary =
                transactionService.getSummary(1L);

        assertEquals(100.0, summary.getTotalAdded());
        assertEquals(30.0, summary.getTotalSent());
        assertEquals(120.0, summary.getTotalReceived());
        assertEquals(90.0, summary.getCurrentBalance());
    }
}