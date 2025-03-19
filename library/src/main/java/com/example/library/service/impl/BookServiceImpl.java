package com.example.library.service.impl;

import com.example.library.domain.entity.AuthorEntity;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.domain.entity.BookEntity;
import com.example.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookEntity saveBook(BookEntity bookEntity) {
        AuthorEntity authorEntity = authorRepository.
                findById(bookEntity.getAuthorEntity().getId()).
                orElseThrow(() -> new RuntimeException("Author not " +
                        "found with ID: " + bookEntity.getAuthorEntity().getId()));

        bookEntity.setAuthorEntity(authorEntity);
        return bookRepository.save(bookEntity);
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(String id) {
        return bookRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    public List<BookEntity> getBooksByAuthor(UUID id) {
        return bookRepository.findByAuthorEntity_Id(id);
    }
}
