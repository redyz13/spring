package com.example.authservice.service.impl;

import com.example.authservice.domain.entity.RoleEntity;
import com.example.authservice.domain.entity.UserEntity;
import com.example.authservice.properties.JwtProperties;
import com.example.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateAccessToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claim("role", userEntity.getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(
                        jwtProperties.getAccessExpirationMinutes(), ChronoUnit.MINUTES))
                )
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                        jwtProperties.getAccessSecret()))
                )
                .compact();
    }

    @Override
    public String generateRefreshToken(UUID refreshTokenId) {
        return Jwts.builder()
                .subject("refresh")
                .claim("tokenId", refreshTokenId.toString())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(
                        jwtProperties.getRefreshExpirationDays(), ChronoUnit.DAYS))
                )
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                        jwtProperties.getRefreshSecret()))
                )
                .compact();
    }

    @Override
    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    isRefreshToken ? jwtProperties.getRefreshSecret() : jwtProperties.getAccessSecret()));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("JWT expired");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT");
        } catch (Exception e) {
            System.out.println("Invalid JWT");
        }
        return false;
    }

    @Override
    public UUID getRefreshTokenId(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getRefreshSecret())))
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            String tokenId = claims.get("tokenId", String.class);
            return UUID.fromString(tokenId);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }
}
