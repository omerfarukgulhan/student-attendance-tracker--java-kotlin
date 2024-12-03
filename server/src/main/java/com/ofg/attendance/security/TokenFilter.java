package com.ofg.attendance.security;

import java.io.IOException;

import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    public TokenFilter(TokenService tokenService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenService = tokenService;
        this.exceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenWithPrefix = request.getHeader("Authorization");
        if (tokenWithPrefix != null) {
            User user = tokenService.verifyToken(tokenWithPrefix).orElse(null);
            if (user != null) {
                if (!user.isActive()) {
                    exceptionResolver.resolveException(request, response, null, new DisabledException("User is disabled"));
                    return;
                }
                CurrentUser currentUser = new CurrentUser(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}