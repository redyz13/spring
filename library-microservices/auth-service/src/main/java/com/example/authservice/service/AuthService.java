package com.example.authservice.service;

import com.example.authservice.domain.dto.AuthRequestDTO;
import com.example.authservice.domain.entity.UserEntity;

public interface AuthService {
    UserEntity register(AuthRequestDTO authRequestDTO);

    String verify(AuthRequestDTO authRequestDTO);
}
