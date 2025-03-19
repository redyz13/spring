package com.example.booksservice.service;

import com.example.booksservice.domain.entity.BookEntity;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookEntity saveBook(BookEntity bookEntity);
    List<BookEntity> getAllBooks();
    BookEntity updateBook(String id, BookEntity bookEntity);
    BookEntity partialUpdateBook(String id, Map<String, Object> updates);
}
