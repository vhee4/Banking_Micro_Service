package com.IdentityRegistry.IdentityRegistry.security;

import com.IdentityRegistry.IdentityRegistry.security.token.TokenRepository;
import com.IdentityRegistry.IdentityRegistry.service.serviceImpl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
        private final JwtTokenProvider jwtTokenProvider;
        private final CustomUserDetailsService customUserDetailsService;
        private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenfromRequest(request);
        var isTokenValid = tokenRepository.findByToken(token).map(t->!t.isExpired() && !t.isRevoked()).orElse(false);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token) && isTokenValid){
            String username = jwtTokenProvider.getUserName(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
        private String getTokenfromRequest(HttpServletRequest request){
            String bearerToken =request.getHeader("Authorization");
            if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
                return bearerToken.substring(7, bearerToken.length());
            }
            return null;
        }

}
