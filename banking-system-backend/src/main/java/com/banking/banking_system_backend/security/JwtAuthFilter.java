package com.banking.banking_system_backend.security;

import com.banking.banking_system_backend.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

            if (jwtUtils.validateJwtToken(token)) {
                username = jwtUtils.getUsernameFromJwtToken(token);

                // 🔍 DEBUG LOGS
                System.out.println("===== JWT DEBUG =====");
                System.out.println("Request URI: " + request.getRequestURI());
                System.out.println("Token received: " + token);
                System.out.println("Username from token: " + username);
                System.out.println("Role from token: " + jwtUtils.getRoleFromToken(token));
                System.out.println("=====================");
            }
        }

        // ✅ Set Spring Security context if username is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String role = jwtUtils.getRoleFromToken(token);

            // 🔹 Always add ROLE_ prefix for Spring Security
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(role));

            // 🔹 Build auth token with proper authorities
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    // Do not filter auth endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth");
    }
}
