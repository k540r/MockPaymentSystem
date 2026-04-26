package com.kapil.mockpaymentsystem.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kapil.mockpaymentsystem.model.BlacklistedToken;

import jakarta.transaction.Transactional;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistedToken b WHERE b.expiry < :time")
    void deleteByExpiryBefore(@Param("time") LocalDateTime time);
}
