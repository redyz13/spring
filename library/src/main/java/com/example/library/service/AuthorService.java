package com.example.library.service;

import com.example.library.domain.entity.AuthorEntity;

import java.util.List;

public interface AuthorService {
    AuthorEntity saveAuthor(AuthorEntity authorEntity);
    List<AuthorEntity> getAllAuthors();
}
