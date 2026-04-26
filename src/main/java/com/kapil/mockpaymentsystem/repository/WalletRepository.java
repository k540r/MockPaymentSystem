package com.kapil.mockpaymentsystem.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByUser(User user);
    @Query("SELECT w FROM Wallet w ORDER BY w.balance DESC")
List<Wallet> findTopUsers(PageRequest pageable);
@Query("SELECT SUM(w.balance) FROM Wallet w")
Double getTotalBalance();

}