package com.example.booksservice.repository;

import com.example.booksservice.domain.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
    Optional<List<BookEntity>> findByAuthorId(UUID authorId);
}
