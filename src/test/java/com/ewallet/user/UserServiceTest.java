package com.ewallet.user;

import com.ewallet.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldSaveUserSuccessfully() {

        User user = new User();
        user.setName("Shivam");
        user.setEmail("shivam@test.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);
        assertEquals("shivam@test.com", savedUser.getEmail());

        verify(userRepository, times(1))
                .save(any(User.class));

        verify(walletService, times(1))
                .createWallet(1L);
    }
}