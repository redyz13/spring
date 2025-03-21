package com.example.authservice.controller;

import com.example.authservice.domain.AuthTokens;
import com.example.authservice.domain.Credentials;
import com.example.authservice.domain.dto.*;
import com.example.authservice.domain.entity.UserEntity;
import com.example.authservice.mapper.Mapper;
import com.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final Mapper<UserEntity, UserDTO> mapper;

    public AuthController(AuthService authService, Mapper<UserEntity, UserDTO> mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO authRequestDTO) {
        Credentials credentials = new Credentials(
                authRequestDTO.getUsername(), authRequestDTO.getPassword()
        );
        UserEntity newUserEntity = authService.register(credentials);
        UserDTO newUserDTO = mapper.toDTO(newUserEntity);
        return new ResponseEntity<>(newUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO) {
        Credentials credentials = new Credentials(
                authRequestDTO.getUsername(), authRequestDTO.getPassword()
        );
        AuthTokens tokens = authService.verify(credentials);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(
                tokens.getAccessToken(),
                tokens.getRefreshToken()
        );
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        authService.logout(logoutRequestDTO.getRefreshToken());
        return ResponseEntity.ok("User logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDTO tokenRefreshRequestDTO) {
        AuthTokens tokens = authService.refreshAccessToken(tokenRefreshRequestDTO.getRefreshToken());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(tokens.getAccessToken(), tokens.getRefreshToken());
        return ResponseEntity.ok(authResponseDTO);
    }
}
