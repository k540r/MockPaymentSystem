package com.kapil.mockpaymentsystem.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kapil.mockpaymentsystem.repository.BlacklistedTokenRepository;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private BlacklistedTokenRepository blacklistRepo;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanExpiredTokens() {

        blacklistRepo.deleteByExpiryBefore(LocalDateTime.now());

        System.out.println("Expired blacklist tokens deleted");
    }
}
