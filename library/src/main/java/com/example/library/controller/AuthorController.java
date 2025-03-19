package com.example.library.controller;

import com.example.library.domain.dto.AuthorDTO;
import com.example.library.domain.entity.AuthorEntity;
import com.example.library.mapper.impl.AuthorMapperImpl;
import com.example.library.service.impl.AuthorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorServiceImpl authorServiceImpl;
    private final AuthorMapperImpl authorMapperImpl;

    public AuthorController(AuthorServiceImpl authorServiceImpl, AuthorMapperImpl authorMapperImpl) {
        this.authorServiceImpl = authorServiceImpl;
        this.authorMapperImpl = authorMapperImpl;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> saveAuthor(
            @RequestBody AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorMapperImpl.toEntity(authorDTO);
        AuthorEntity savedAuthorEntity = authorServiceImpl.saveAuthor(authorEntity);
        AuthorDTO savedAuthorDTO = authorMapperImpl.toDTO(savedAuthorEntity);
        return new ResponseEntity<>(savedAuthorDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorMapperImpl.toDTO(authorServiceImpl.getAllAuthors());
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable UUID id) {
        AuthorEntity authorEntity = authorServiceImpl.getAuthorById(id);
        return authorMapperImpl.toDTO(authorEntity);
    }
}