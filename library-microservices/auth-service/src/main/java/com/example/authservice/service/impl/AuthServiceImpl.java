package com.example.authservice.service.impl;

import com.example.authservice.domain.dto.AuthRequestDTO;
import com.example.authservice.domain.entity.RoleEntity;
import com.example.authservice.domain.entity.TokenEntity;
import com.example.authservice.domain.entity.UserEntity;
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
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-role}")
    private String defaultRole;

    @Value("${jwt.expirationHours}")
    private int expirationHours;

    public AuthServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository,
                           AuthenticationManager authenticationManager, JwtService jwtService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity register(AuthRequestDTO authRequestDTO) {
        userRepository.findByUsername(authRequestDTO.getUsername())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
                });

        String hashedPassword = passwordEncoder.encode(authRequestDTO.getPassword());

        RoleEntity userRole = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found: " + defaultRole));

        UserEntity newUser = UserEntity.builder()
                .username(authRequestDTO.getUsername())
                .password(hashedPassword)
                // TODO .verified(false)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public String verify(AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        UserEntity userEntity = userRepository.findByUsername(authRequestDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        /* TODO if (!userEntity.isVerified())
            throw new IllegalArgumentException("User not verified"); */

        String token = jwtService.generateToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
                .token(token)
                .user(userEntity)
                .expiryDate(LocalDateTime.now().plusHours(expirationHours))
                .build();
        tokenRepository.save(tokenEntity);

        return token;
    }
}
