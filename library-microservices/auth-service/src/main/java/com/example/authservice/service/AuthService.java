package com.example.authservice.service;

import com.example.authservice.domain.AuthTokens;
import com.example.authservice.domain.Credentials;
import com.example.authservice.domain.entity.UserEntity;

import java.util.UUID;


public interface AuthService {
    UserEntity register(Credentials credentials);

    AuthTokens verify(Credentials credentials);

    void logout(String refreshToken);

    AuthTokens refreshAccessToken(String refreshToken);
}
