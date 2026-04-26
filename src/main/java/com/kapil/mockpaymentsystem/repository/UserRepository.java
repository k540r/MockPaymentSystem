package com.kapil.mockpaymentsystem.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kapil.mockpaymentsystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //  Active / Inactive filter
    List<User> findByActive(boolean active);

    // Dashboard count
    long countByActive(boolean active);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    public Page<User> findAll(Pageable pageable);
   
}
