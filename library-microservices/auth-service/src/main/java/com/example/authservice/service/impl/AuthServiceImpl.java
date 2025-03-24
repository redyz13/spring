package com.example.authservice.service.impl;

import com.example.authservice.domain.AuthTokens;
import com.example.authservice.domain.Credentials;
import com.example.authservice.domain.entity.RoleEntity;
import com.example.authservice.domain.entity.TokenEntity;
import com.example.authservice.domain.entity.UserEntity;
import com.example.authservice.properties.JwtProperties;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-role}")
    private String defaultRole;

    public AuthServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository,
                           AuthenticationManager authenticationManager, JwtService jwtService, JwtProperties jwtProperties,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity register(Credentials credentials) {
        userRepository.findByUsername(credentials.getUsername())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
                });

        String hashedPassword = passwordEncoder.encode(credentials.getPassword());

        RoleEntity userRole = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found: " + defaultRole));

        UserEntity newUser = UserEntity.builder()
                .username(credentials.getUsername())
                .password(hashedPassword)
                // TODO .verified(false)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public AuthTokens verify(Credentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        UserEntity userEntity = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        /* TODO if (!userEntity.isVerified())
            throw new IllegalArgumentException("User not verified"); */

        TokenEntity refreshTokenEntity = TokenEntity.builder()
                .user(userEntity)
                .expiryDate(LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays()))
                .revoked(false)
                .build();

        refreshTokenEntity = tokenRepository.save(refreshTokenEntity);

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(refreshTokenEntity.getId());

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        UUID tokenId = jwtService.getRefreshTokenId(refreshToken);

        TokenEntity tokenEntity = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        if (tokenEntity.isRevoked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token already revoked");

        tokenRepository.updateRevokedById(true, tokenId);
    }

    @Override
    public AuthTokens refreshAccessToken(String refreshToken) {
        UUID refreshTokenId = jwtService.getRefreshTokenId(refreshToken);

        TokenEntity oldToken = tokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (oldToken.isRevoked() || oldToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired or revoked");

        oldToken.setRevoked(true);
        tokenRepository.save(oldToken);

        TokenEntity newToken = TokenEntity.builder()
                .user(oldToken.getUser())
                .expiryDate(LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays()))
                .revoked(false)
                .build();
        tokenRepository.save(newToken);

        String newAccessToken = jwtService.generateAccessToken(newToken.getUser());
        String newRefreshToken = jwtService.generateRefreshToken(newToken.getId());

        return new AuthTokens(newAccessToken, newRefreshToken);
    }
}
