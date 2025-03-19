package com.example.booksservice.client;

import com.example.booksservice.domain.dto.AuthorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "authors-service", path = "/authors")
public interface AuthorClient {
    @GetMapping("/{id}")
    AuthorDTO getAuthorById(@PathVariable UUID id);
}
