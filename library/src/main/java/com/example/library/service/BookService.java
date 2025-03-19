package com.example.library.service;

import com.example.library.domain.entity.BookEntity;

import java.util.List;

public interface BookService {
    BookEntity saveBook(BookEntity bookEntity);
    List<BookEntity> getAllBooks();
}
