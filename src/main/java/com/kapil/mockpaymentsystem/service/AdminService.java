package com.kapil.mockpaymentsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kapil.mockpaymentsystem.dto.UserDTO;
import com.kapil.mockpaymentsystem.model.Transaction;
import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.model.Wallet;
import com.kapil.mockpaymentsystem.repository.TransactionRepository;
import com.kapil.mockpaymentsystem.repository.UserRepository;
import com.kapil.mockpaymentsystem.repository.WalletRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
     private final TransactionRepository transactionRepository;

    public AdminService(UserRepository userRepository, TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    //  Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user
  public void deleteUser(String adminUsername, String targetUsername) {

    if (adminUsername.equals(targetUsername)) {
        throw new RuntimeException("Admin cannot delete himself");
    }

    User user = userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setActive(false);
    userRepository.save(user);
}
public void activateUser(String adminUsername, String targetUsername) {

    if (adminUsername.equals(targetUsername)) {
        throw new RuntimeException("Admin cannot activate himself unnecessarily");
    }

    User user = userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setActive(true);
    userRepository.save(user);
}
public Page<Transaction> getTransactionsByType(String type, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return transactionRepository.findByType(type, pageable);
}


    // Promote user
    public void promoteToAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ADMIN");
        userRepository.save(user);
    }

public List<User> getActiveUsers() {
    return userRepository.findByActive(true);
}
public List<User> getInactiveUsers() {
    return userRepository.findByActive(false);
}
 

    public List<Wallet> getTopUsers() {
    return walletRepository.findTopUsers(PageRequest.of(0, 5));
}

private UserDTO mapToDTO(User user) {

    UserDTO dto = new UserDTO();

    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setRole(user.getRole());
    dto.setActive(user.isActive());

    return dto;
}

//pagination
public Page<UserDTO> getUsers(String search, int page, int size, String sortBy, String direction) {

    Sort sort = direction.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<User> users;

    if (search != null && !search.isEmpty()) {
        users = userRepository.findByUsernameContainingIgnoreCase(search, pageable);
    } else {
        users = userRepository.findAll(pageable);
    }

    return users.map(this::mapToDTO);
}
public Map<String, Object> getDashboard() {

    Map<String, Object> map = new HashMap<>();

    map.put("totalUsers", userRepository.count());
    map.put("activeUsers", userRepository.countByActive(true));
    map.put("inactiveUsers", userRepository.countByActive(false));

    map.put("totalBalance", walletRepository.getTotalBalance());
    map.put("totalTransactions", transactionRepository.count());

    return map;
}
}
