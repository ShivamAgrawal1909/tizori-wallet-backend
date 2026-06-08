package com.ewallet.admin;

import com.ewallet.transaction.TransactionRepository;
import com.ewallet.user.UserRepository;
import com.ewallet.wallet.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AdminController adminController;

    @Test
    void dashboard_ShouldReturnCorrectCounts() {

        when(userRepository.count()).thenReturn(5L);
        when(walletRepository.count()).thenReturn(5L);
        when(transactionRepository.count()).thenReturn(20L);

        Map<String, Object> stats =
                adminController.getDashboardStats();

        assertEquals(5L, stats.get("totalUsers"));
        assertEquals(5L, stats.get("totalWallets"));
        assertEquals(20L, stats.get("totalTransactions"));

        verify(userRepository, times(1)).count();
        verify(walletRepository, times(1)).count();
        verify(transactionRepository, times(1)).count();
    }
}