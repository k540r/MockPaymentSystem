package com.kapil.mockpaymentsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kapil.mockpaymentsystem.model.Transaction;
import com.kapil.mockpaymentsystem.model.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findBySenderOrReceiver(User sender, User receiver, Pageable pageable);

    Page<Transaction> findByType(String type, Pageable pageable);

    Page<Transaction> findByStatus(String status, Pageable pageable);

    Page<Transaction> findBySenderUsernameOrReceiverUsername(
            String sender,
            String receiver,
            Pageable pageable
    );

    long count();
}
