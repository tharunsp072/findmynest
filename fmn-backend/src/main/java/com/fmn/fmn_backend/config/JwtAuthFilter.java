package com.fmn.fmn_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AppConfig appConfig;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getMethod().equalsIgnoreCase("OPTIONS") || request.getServletPath().startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = appConfig.userDetailsService().loadUserByUsername(username);

            if (jwtUtils.validateToken(jwt)) {
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + jwtUtils.getRoleFromToken(jwt)));
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));                SecurityContextHolder.getContext().setAuthentication(authToken);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        System.out.println("JwtAuthFilter applied for: " + request.getServletPath());
        filterChain.doFilter(request, response);
    }
}
