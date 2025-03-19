package com.example.authorsservice.service;

import com.example.authorsservice.domain.entity.AuthorEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AuthorService {
    AuthorEntity saveAuthor(AuthorEntity authorEntity);
    List<AuthorEntity> getAllAuthors();
    AuthorEntity updateAuthor(UUID id, AuthorEntity authorEntity);
    AuthorEntity partialUpdateAuthor(UUID id, Map<String, Object> updates);
}
