package com.saraasansor.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = getJwtFromRequest(request);
        String requestPath = request.getRequestURI();
        
        // Debug logging
        System.out.println("🔵 JWT Filter - Request: " + request.getMethod() + " " + requestPath);
        System.out.println("🔵 JWT Filter - Authorization header: " + (request.getHeader("Authorization") != null ? "present" : "missing"));
        System.out.println("🔵 JWT Filter - JWT token: " + (jwt != null ? jwt.substring(0, Math.min(20, jwt.length())) + "..." : "null"));
        
        // Only validate JWT if token is present
        if (StringUtils.hasText(jwt)) {
            try {
                String username = tokenProvider.getUsernameFromToken(jwt);
                System.out.println("🔵 JWT Filter - Extracted username: " + username);
                
                if (tokenProvider.validateToken(jwt, username)) {
                    System.out.println("✅ JWT Filter - Token validated successfully");
                    
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("✅ JWT Filter - UserDetails loaded: " + userDetails.getUsername() + ", Authorities: " + userDetails.getAuthorities());
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ JWT Filter - Authentication set in SecurityContext");
                } else {
                    System.out.println("❌ JWT Filter - Token validation failed");
                }
            } catch (Exception e) {
                // Invalid token - clear security context and continue
                System.err.println("❌ JWT Filter - Exception during token validation: " + e.getMessage());
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("⚠️ JWT Filter - No JWT token found in request");
        }
        
        // Check SecurityContext after processing
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("✅ JWT Filter - SecurityContext has authentication: " + SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            System.out.println("⚠️ JWT Filter - SecurityContext has NO authentication");
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

