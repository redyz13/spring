package com.example.booksservice.service;

import com.example.booksservice.domain.entity.BookEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookService {
    BookEntity saveBook(BookEntity bookEntity);
    void deleteBook(String id);
    void deleteBooksByAuthor(UUID authorId);
    List<BookEntity> getAllBooks();
    BookEntity updateBook(String id, BookEntity bookEntity);
    BookEntity partialUpdateBook(String id, Map<String, Object> updates);
    BookEntity getBookById(String id);
    List<BookEntity> getBooksByAuthor(UUID authorId);
}
