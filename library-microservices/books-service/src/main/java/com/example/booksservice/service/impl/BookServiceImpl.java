package com.example.booksservice.service.impl;

import com.example.booksservice.annotation.ForeignKeyField;
import com.example.booksservice.client.AuthorClient;
import com.example.booksservice.domain.dto.AuthorDTO;
import com.example.booksservice.domain.entity.BookEntity;
import com.example.booksservice.repository.BookRepository;
import com.example.booksservice.service.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorClient authorClient;

    @Override
    public BookEntity saveBook(BookEntity bookEntity) {
        AuthorDTO authorDTO = authorClient.getAuthorById(bookEntity.getAuthorId());

        if (bookRepository.existsById(bookEntity.getIsbn()))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Book already exists with ISBN: " + bookEntity.getIsbn());

        if (authorDTO == null)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Author not found with ID: " + bookEntity.getAuthorId());

        bookEntity.setAuthorId(authorDTO.getId());
        return bookRepository.save(bookEntity);
    }

    @Override
    public void deleteBook(String id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with ID: " + id));
        bookRepository.delete(bookEntity);
    }

    @Override
    @Transactional
    public void deleteBooksByAuthor(UUID authorId) {
        List<BookEntity> books = bookRepository.findByAuthorId(authorId);
        if (!books.isEmpty())
            bookRepository.deleteAllInBatch(books);
    }

    @Override
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookEntity updateBook(String id, BookEntity bookEntity) {
        AuthorDTO authorDTO = authorClient.getAuthorById(bookEntity.getAuthorId());

        if (authorDTO == null)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Author not found with ID: " + bookEntity.getAuthorId());

        return bookRepository.findById(id)
                .map(existingBook -> bookRepository.save(existingBook
                        .toBuilder()
                        .title(bookEntity.getTitle())
                        .authorId(bookEntity.getAuthorId())
                        .build()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with ID: " + id)
                );
    }

    @Override
    public BookEntity partialUpdateBook(String id, Map<String, Object> updates) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(existingBook.getClass(), key);
                        if (field != null) {
                            field.setAccessible(true);

                            if (field.isAnnotationPresent(ForeignKeyField.class)) {
                                AuthorDTO authorDTO = authorClient.getAuthorById(
                                        UUID.fromString(value.toString()));
                                if (authorDTO == null)
                                    throw new ResponseStatusException(
                                            HttpStatus.NOT_FOUND, "Author not found with ID: " + value);
                            }

                            ReflectionUtils.setField(field, existingBook, value);
                        }
                    });
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with ID: " + id));
    }

    @Override
    public BookEntity getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with ID: " + id));
    }

    @Override
    public List<BookEntity> getBooksByAuthor(UUID id) {
        return bookRepository.findByAuthorId(id);
    }
}
