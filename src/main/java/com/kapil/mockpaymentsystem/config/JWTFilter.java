// package com.kapil.mockpaymentsystem.config;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class JWTFilter extends OncePerRequestFilter {

//     private final JWTUtil jwtUtil;

//     public JWTFilter(JWTUtil jwtUtil) {
//         this.jwtUtil = jwtUtil;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         String path = request.getRequestURI();

        
//         if (path.startsWith("/api/auth")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String header = request.getHeader("Authorization");

    
//         if (header == null || !header.startsWith("Bearer ")) {
//             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing");
//             return;
//         }

//         try {
//            String token = header.substring(7);
// String username = jwtUtil.extractUsername(token);
// String role = jwtUtil.extractUsername(token);

// if (username != null) {

//     List<SimpleGrantedAuthority> authorities = List.of(
//             new SimpleGrantedAuthority("ROLE_" + role)
//     );

//     UsernamePasswordAuthenticationToken auth =
//             new UsernamePasswordAuthenticationToken(
//                     username, null, authorities);

//     SecurityContextHolder.getContext().setAuthentication(auth);
// }

//         } catch (Exception e) {
            
//             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//             return;
//         }

//         filterChain.doFilter(request, response);
//     }
//     public String extractRole(String token) {
//     Claims claims = Jwts.parserBuilder()
//             .setSigningKey(key)
//             .build()
//             .parseClaimsJws(token)
//             .getBody();

//     return claims.get("role", String.class);
// }
// }

package com.kapil.mockpaymentsystem.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kapil.mockpaymentsystem.repository.BlacklistedTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
private BlacklistedTokenRepository blacklistRepo;

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        //Allow auth APIs without token
        if (path.startsWith("/api/auth")|| path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        //No token
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing");
            return;
        }

        try {
            String token = header.substring(7);
             if (blacklistRepo.existsByToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
            return;
        }

            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);
           
            if (username != null) {

                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
    
}