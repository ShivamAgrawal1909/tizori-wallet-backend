package com.ewallet.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    // Wallet banao
    @PostMapping("/create/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.createWallet(userId));
    }

    // Balance dekho
    @GetMapping("/balance/{userId}")
    public ResponseEntity<Wallet> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWallet(userId));
    }

    // Paisa add karo
    @PostMapping("/add/{userId}")
    public ResponseEntity<Wallet> addMoney(@PathVariable Long userId,
                                           @RequestParam Double amount) {
        return ResponseEntity.ok(walletService.addMoney(userId, amount));
    }

    // Paisa transfer karo
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromUserId,
                                           @RequestParam Long toUserId,
                                           @RequestParam Double amount) {
        return ResponseEntity.ok(walletService.transferMoney(fromUserId, toUserId, amount));
    }
}