package com.IdentityRegistry.IdentityRegistry.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
        private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

        @Value("${app.jwt-secret}")
        private String jwtSecret;

        @Value("${app.jwt-expiration-milliseconds}")
        private Long jwtExpiration;

        private Key key(){
            byte[] bytes = Decoders.BASE64URL.decode(jwtSecret);
            return Keys.hmacShaKeyFor(bytes);
        }

        public String generateToken(Authentication authentication){
            String username = authentication.getName();
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + jwtExpiration);
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(currentDate)
                    .setExpiration(expireDate)
                    .signWith(key())
                    .compact();
        }

        //Extract username from the generated token
        public String getUserName(String token){
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token).getBody();
            return claims.getSubject();
        }

        //validate the generated token
        public  boolean validateToken(String token){
            try {
                Jwts.parserBuilder()
                        .setSigningKey(key())
                        .build()
                        .parse(token);
                return true;
            }catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
                throw new RuntimeException(e);
            }
        }

}
