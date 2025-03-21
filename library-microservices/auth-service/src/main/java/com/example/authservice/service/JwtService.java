package com.example.authservice.service;

import com.example.authservice.domain.entity.UserEntity;

public interface JwtService {
    String generateToken(UserEntity userEntity);

    boolean validateToken(String token);
}
