package com.kapil.mockpaymentsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kapil.mockpaymentsystem.dto.UserDTO;
import com.kapil.mockpaymentsystem.model.Transaction;
import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.model.Wallet;
import com.kapil.mockpaymentsystem.service.AdminService;
import com.kapil.mockpaymentsystem.service.WalletService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") 
public class AdminController {

        private final WalletService walletService;
    
        private final AdminService adminService;


    public AdminController(AdminService adminService, WalletService walletService) {
        this.adminService = adminService;
        this.walletService = walletService;
    }

    
   @GetMapping("/users")
public Page<UserDTO> getUsers(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "username") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
) {
    return adminService.getUsers(search, page, size, sortBy, direction);
}

    
    @DeleteMapping("/delete")
public String deleteUser(
        @RequestParam String adminUsername,
        @RequestParam String username
) {
    adminService.deleteUser(adminUsername, username);
    return "User deleted successfully";
}
    @PostMapping("/activate")
public String activateUser(
        @RequestParam String adminUsername,
        @RequestParam String username
) {
    adminService.activateUser(adminUsername, username);
    return "User activated successfully";
}
@GetMapping("/users/active")
public List<User> getActiveUsers() {
    return adminService.getActiveUsers();
}
@GetMapping("/users/inactive")
public List<User> getInactiveUsers() {
    return adminService.getInactiveUsers();
}
@GetMapping("/dashboard")
public Map<String, Object> dashboard() {
    return adminService.getDashboard();
}
@GetMapping("/transactions/status")
public Page<Transaction> getByStatus(
        @RequestParam String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return walletService.getByStatus(status, page, size);
}

    @PostMapping("/promote")
    public String promoteUser(@RequestParam String username) {
        adminService.promoteToAdmin(username);
        return "User promoted to ADMIN";
    }

    @GetMapping("/wallet/balance")
public Double getUserBalance(@RequestParam String username) {
    return walletService.getBalance(username);
}
@PostMapping("/wallet/deposit")
public String depositToUser(@RequestParam String username,
                            @RequestParam Double amount) {
    return walletService.deposit(username, amount);
}
@PostMapping("/wallet/withdraw")
public String withdrawFromUser(@RequestParam String username,
                               @RequestParam Double amount) {
    return walletService.withdraw(username, amount);
}
@PostMapping("/wallet/transfer")
public String transferBetweenUsers(@RequestParam String from,
                                   @RequestParam String to,
                                   @RequestParam Double amount) {

    return walletService.transfer(from, to, amount);
}

@GetMapping("/wallet/transactions")
public Page<Transaction> getUserTransactions(
        @RequestParam String username,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Double minAmount,
        @RequestParam(required = false) Double maxAmount,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return walletService.getTransactions(username, type, minAmount, maxAmount, page, size);
}
@GetMapping("/transactions/type")
public Page<Transaction> getByType(
        @RequestParam String type,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return adminService.getTransactionsByType(type, page, size);
}
@GetMapping("/top-users")
public List<Wallet> topUsers() {
    return adminService.getTopUsers();
}
}