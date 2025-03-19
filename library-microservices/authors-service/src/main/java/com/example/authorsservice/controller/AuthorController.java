package com.example.authorsservice.controller;

import com.example.authorsservice.domain.dto.AuthorDTO;
import com.example.authorsservice.domain.entity.AuthorEntity;
import com.example.authorsservice.mapper.Mapper;
import com.example.authorsservice.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final Mapper<AuthorEntity, AuthorDTO> mapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDTO> mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> saveAuthor(
            @RequestBody AuthorDTO authorDTO) {
        AuthorEntity authorEntity = mapper.toEntity(authorDTO);
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);
        AuthorDTO savedAuthorDTO = mapper.toDTO(savedAuthorEntity);
        return new ResponseEntity<>(savedAuthorDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable UUID id, @RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(mapper
                .toDTO(authorService
                        .updateAuthor(id, mapper
                                .toEntity(authorDTO))), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(@PathVariable UUID id,
                                                         @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(mapper
                .toDTO(authorService
                        .partialUpdateAuthor(id, updates)), HttpStatus.OK);
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return mapper.toDTO(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable UUID id) {
        AuthorEntity authorEntity = authorService.getAuthorById(id);
        return mapper.toDTO(authorEntity);
    }
}