package com.example.authorsservice.service.impl;

import com.example.authorsservice.domain.entity.AuthorEntity;
import com.example.authorsservice.repository.AuthorRepository;
import com.example.authorsservice.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository)  {
        this.authorRepository = authorRepository;
    }

    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    public List<AuthorEntity> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public AuthorEntity updateAuthor(UUID id, AuthorEntity authorEntity) {
        return authorRepository.findById(id)
                .map(existingAuthor -> authorRepository.save(existingAuthor
                        .toBuilder()
                        .name(authorEntity.getName())
                        .age(authorEntity.getAge())
                        .build()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Author not found with ID: " + id));
    }


    public AuthorEntity partialUpdateAuthor(UUID id, Map<String, Object> updates) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(existingAuthor.getClass(), key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, existingAuthor, value);
                        }
                    });
                    return authorRepository.save(existingAuthor);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Author not found with ID: " + id));
    }

    public AuthorEntity getAuthorById(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Author not found with ID: " + id));
    }
}
