package com.kapil.mockpaymentsystem.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kapil.mockpaymentsystem.model.Transaction;
import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.model.Wallet;
import com.kapil.mockpaymentsystem.repository.TransactionRepository;
import com.kapil.mockpaymentsystem.repository.UserRepository;
import com.kapil.mockpaymentsystem.repository.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository,
                         TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Double getBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                if (!user.isActive()) {
        throw new RuntimeException("User is deactivated");
    }
        Wallet wallet = walletRepository.findByUser(user);
        return wallet.getBalance();
    }

    public String deposit(String username, Double amount) {
       if (amount <= 0) {
    return "FAILED: Amount must be greater than 0";
}
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                if (!user.isActive()) {
    throw new RuntimeException("User is deactivated");
}
        Wallet wallet = walletRepository.findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        Transaction txn = new Transaction();
        txn.setSender(null);
        txn.setReceiver(user);
        txn.setAmount(amount);
        txn.setDateTime(LocalDateTime.now());
        txn.setStatus("SUCCESS");
        txn.setType("DEPOSIT");
        transactionRepository.save(txn);

        return "Deposit successful. Current balance: " + wallet.getBalance();
    }

    public String withdraw(String username, Double amount) {
    if (amount == null || amount <= 0) {
        throw new IllegalArgumentException("Amount must be greater than 0");
    }

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.isActive()) {
        throw new RuntimeException("User is deactivated");
    }

    Wallet wallet = walletRepository.findByUser(user);

    if (wallet == null) {
        throw new RuntimeException("Wallet not found");
    }

    if (wallet.getBalance() < amount) {

        Transaction failedTxn = new Transaction();
        failedTxn.setSender(user);
        failedTxn.setAmount(amount);
        failedTxn.setStatus("FAILED");
        failedTxn.setType("WITHDRAW");
        failedTxn.setDateTime(LocalDateTime.now());

        transactionRepository.save(failedTxn);

        throw new RuntimeException("Insufficient balance");
    }

    wallet.setBalance(wallet.getBalance() - amount);
    walletRepository.save(wallet);

    Transaction txn = new Transaction();
    txn.setSender(user);
    txn.setAmount(amount);
    txn.setStatus("SUCCESS");
    txn.setType("WITHDRAW");
    txn.setDateTime(LocalDateTime.now());
    transactionRepository.save(txn);

    return "Withdrawal successful. Current balance: " + wallet.getBalance();
}
    public String transfer(String senderUsername, String receiverUsername, Double amount) {
        if (amount <= 0) throw new RuntimeException("Amount must be positive");

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
                if (!sender.isActive()) {
    throw new RuntimeException("Sender is deactivated");
}

if (!receiver.isActive()) {
    throw new RuntimeException("Receiver is deactivated");
}

        Wallet senderWallet = walletRepository.findByUser(sender);
        Wallet receiverWallet = walletRepository.findByUser(receiver);

        if (senderWallet.getBalance() < amount) throw new RuntimeException("Insufficient balance");

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction txn = new Transaction();
        txn.setSender(sender);
        txn.setReceiver(receiver);
        txn.setAmount(amount);
        txn.setDateTime(LocalDateTime.now());
        txn.setStatus("SUCCESS");
        txn.setType("TRANSFER");
        transactionRepository.save(txn);

        return "Transfer successful. Your balance: " + senderWallet.getBalance();
    }

   public Page<Transaction> getTransactions(
        String username,
        String type,
        Double minAmount,
        Double maxAmount,
        int page,
        int size
) {

    Pageable pageable = PageRequest.of(page, size);

    return transactionRepository
            .findBySenderUsernameOrReceiverUsername(username, username, pageable);
}

public Page<Transaction> getByStatus(String status, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return transactionRepository.findByStatus(status, pageable);
}

private User getActiveUser(String username) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.isActive()) {
        throw new RuntimeException("User is deactivated");
    }

    return user;
}
}