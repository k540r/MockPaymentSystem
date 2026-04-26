package com.kapil.mockpaymentsystem.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kapil.mockpaymentsystem.config.JWTUtil;
import com.kapil.mockpaymentsystem.model.BlacklistedToken;
import com.kapil.mockpaymentsystem.model.RefreshToken;
import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.model.Wallet;
import com.kapil.mockpaymentsystem.repository.BlacklistedTokenRepository;
import com.kapil.mockpaymentsystem.repository.RefreshTokenRepository;
import com.kapil.mockpaymentsystem.repository.UserRepository;
import com.kapil.mockpaymentsystem.repository.WalletRepository;

@Service
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BlacklistedTokenRepository blacklistTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

   public AuthService(UserRepository userRepository,
                   WalletRepository walletRepository,
                   BlacklistedTokenRepository blacklistTokenRepository,
                   BCryptPasswordEncoder passwordEncoder,
                   JWTUtil jwtUtil,
                   RefreshTokenRepository refreshTokenRepository) {

    this.userRepository = userRepository;
    this.walletRepository = walletRepository;
    this.blacklistTokenRepository=blacklistTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.refreshTokenRepository = refreshTokenRepository;
}

    
    public User register(String username, String email, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

       
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        return savedUser;
    }

    
    public Map<String, String> login(String username, String password) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.isActive()) {
        throw new RuntimeException("User is deactivated");
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    //  ACCESS TOKEN
    String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());

    // REFRESH TOKEN
    String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

    //SAVE refresh token in DB
    RefreshToken rt = new RefreshToken();
    rt.setToken(refreshToken);
    rt.setUsername(user.getUsername());
    rt.setExpiryDate(java.time.LocalDateTime.now().plusDays(7));

    refreshTokenRepository.save(rt);

    //RESPONSE
    Map<String, String> response = new java.util.HashMap<>();
    response.put("accessToken", accessToken);
    response.put("refreshToken", refreshToken);
    response.put("role", user.getRole());
    response.put("username", user.getUsername());

    return response;
}
public String refreshAccessToken(String refreshToken) {

    RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (token.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
        throw new RuntimeException("Refresh token expired");
    }

    return jwtUtil.generateToken(token.getUsername(), "USER");
}
public String logout(String refreshToken, String accessToken) {

    // delete refresh token
    RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    refreshTokenRepository.delete(token);

    // blacklist access token
    BlacklistedToken black = new BlacklistedToken();
    black.setToken(accessToken);
    black.setExpiry(LocalDateTime.now().plusHours(1)); // same as JWT expiry

    blacklistTokenRepository.save(black);

    return "Logged out successfully";
}
}