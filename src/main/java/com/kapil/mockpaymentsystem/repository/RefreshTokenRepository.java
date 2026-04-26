package com.kapil.mockpaymentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kapil.mockpaymentsystem.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsername(String username);
}
