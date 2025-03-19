package com.example.authorsservice.controller;

import com.example.authorsservice.domain.dto.AuthorDTO;
import com.example.authorsservice.domain.entity.AuthorEntity;
import com.example.authorsservice.mapper.impl.AuthorMapperImpl;
import com.example.authorsservice.service.impl.AuthorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable UUID id, @RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(authorMapperImpl
                .toDTO(authorServiceImpl
                        .updateAuthor(id, authorMapperImpl
                                .toEntity(authorDTO))), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(@PathVariable UUID id,
                                                         @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(authorMapperImpl
                .toDTO(authorServiceImpl
                        .partialUpdateAuthor(id, updates)), HttpStatus.OK);
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