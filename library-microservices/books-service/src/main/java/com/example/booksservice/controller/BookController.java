package com.example.booksservice.controller;

import com.example.booksservice.mapper.Mapper;
import com.example.booksservice.service.BookService;
import com.example.booksservice.domain.dto.BookDTO;
import com.example.booksservice.domain.entity.BookEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final Mapper<BookEntity, BookDTO> mapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDTO> mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<BookDTO> saveBook(
            @RequestBody BookDTO bookDTO) {
        BookEntity bookEntity = mapper.toEntity(bookDTO);
        BookEntity savedBookEntity = bookService.saveBook(bookEntity);
        BookDTO savedBookDTO = mapper.toDTO(savedBookEntity);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<Void> deleteBooksByAuthor(@PathVariable("id") UUID authorId) {
        bookService.deleteBooksByAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(mapper
                .toDTO(bookService
                        .updateBook(id, mapper
                                .toEntity(bookDTO))), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> partialUpdateBook(@PathVariable String id,
                                                     @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(mapper
                .toDTO(bookService
                        .partialUpdateBook(id, updates)), HttpStatus.OK);
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return mapper.toDTO(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable String id) {
        BookEntity bookEntity = bookService.getBookById(id);
        return mapper.toDTO(bookEntity);
    }

    @GetMapping("/author/{id}")
    public List<BookDTO> getBooksByAuthor(@PathVariable("id") UUID authorId) {
        return mapper.toDTO(bookService.getBooksByAuthor(authorId));
    }
}
