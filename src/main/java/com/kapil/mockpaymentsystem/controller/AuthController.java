package com.kapil.mockpaymentsystem.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kapil.mockpaymentsystem.model.User;
import com.kapil.mockpaymentsystem.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
 
    @PostMapping("/register")
public String register(@RequestBody User user) {
    User savedUser = authService.register(
            user.getUsername(),
            user.getEmail(),
            user.getPassword()
            
    );

    return "User registered successfully: " + savedUser.getUsername();
}

    //LOGIN (JWT TOKEN GENERATION)
   @PostMapping("/login")
public Map<String, String> login(@RequestBody User user) {
    return authService.login(user.getUsername(), user.getPassword());
}
@PostMapping("/refresh")
public String refresh(@RequestParam String refreshToken) {
    return authService.refreshAccessToken(refreshToken);
}
@PostMapping("/logout")
public String logout(
        @RequestParam String refreshToken,
        @RequestHeader("Authorization") String authHeader
) {

    String accessToken = authHeader.substring(7); // remove "Bearer "

    return authService.logout(refreshToken, accessToken);
}
}