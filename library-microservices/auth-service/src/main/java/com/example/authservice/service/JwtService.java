package com.example.authservice.service;

import com.example.authservice.domain.entity.UserEntity;

import java.util.UUID;

public interface JwtService {
    String generateAccessToken(UserEntity userEntity);

    String generateRefreshToken(UUID refreshTokenId);

    boolean validateToken(String token, boolean isRefreshToken);

    UUID getRefreshTokenId(String refreshToken);
}
