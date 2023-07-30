package com.IdentityRegistry.IdentityRegistry.security;

import com.IdentityRegistry.IdentityRegistry.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String bearerToken =request.getHeader("Authorization");
        final String token;
        if(!StringUtils.hasText(bearerToken) && !bearerToken.startsWith("Bearer ")){
            return;
        }
        token=bearerToken.substring(7);
        var storedToken = tokenRepository.findByToken(token).orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

    }
}
