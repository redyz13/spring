package com.example.authorsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "books-service", path = "/books")
public interface BookClient {
    @DeleteMapping("/author/{id}")
    ResponseEntity<Void> deleteBooksByAuthor(@PathVariable UUID id);
}
