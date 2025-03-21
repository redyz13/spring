package com.example.authservice.controller;

import com.example.authservice.domain.dto.AuthRequestDTO;
import com.example.authservice.domain.dto.AuthResponseDTO;
import com.example.authservice.domain.dto.UserDTO;
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
        UserEntity newUserEntity = authService.register(authRequestDTO);
        UserDTO newUserDTO = mapper.toDTO(newUserEntity);
        return new ResponseEntity<>(newUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO) {
        String token = authService.verify(authRequestDTO);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }
}
