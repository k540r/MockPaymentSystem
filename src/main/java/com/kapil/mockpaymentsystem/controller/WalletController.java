package com.kapil.mockpaymentsystem.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kapil.mockpaymentsystem.model.Transaction;
import com.kapil.mockpaymentsystem.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

   
   @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@GetMapping("/balance")
public Double getBalance() {

    String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return walletService.getBalance(username);
}
    
   @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@PostMapping("/deposit")
public String deposit(@RequestParam Double amount) {

    String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return walletService.deposit(username, amount);
}
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@PostMapping("/withdraw")
public String withdraw(@RequestParam Double amount) {

    String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return walletService.withdraw(username, amount);
}

    
   @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@PostMapping("/transfer")
public String transfer(@RequestParam String receiver,
                       @RequestParam Double amount) {

    String sender = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return walletService.transfer(sender, receiver, amount);
}

    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@GetMapping("/transactions")
public Page<Transaction> getTransactions(
        @RequestParam String username,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Double minAmount,
        @RequestParam(required = false) Double maxAmount,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return walletService.getTransactions(username, type, minAmount, maxAmount, page, size);
}
}