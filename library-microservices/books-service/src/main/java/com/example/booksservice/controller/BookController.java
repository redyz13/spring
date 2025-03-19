package com.example.booksservice.controller;

import com.example.booksservice.mapper.impl.BookMapperImpl;
import com.example.booksservice.service.impl.BookServiceImpl;
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
    private final BookServiceImpl bookServiceImpl;
    private final BookMapperImpl bookMapperImpl;

    public BookController(BookServiceImpl bookServiceImpl, BookMapperImpl bookMapperImpl) {
        this.bookServiceImpl = bookServiceImpl;
        this.bookMapperImpl = bookMapperImpl;
    }

    @PostMapping
    public ResponseEntity<BookDTO> saveBook(
            @RequestBody BookDTO bookDTO) {
        BookEntity bookEntity = bookMapperImpl.toEntity(bookDTO);
        BookEntity savedBookEntity = bookServiceImpl.saveBook(bookEntity);
        BookDTO savedBookDTO = bookMapperImpl.toDTO(savedBookEntity);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookMapperImpl
                .toDTO(bookServiceImpl
                        .updateBook(id, bookMapperImpl
                                .toEntity(bookDTO))), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> partialUpdateBook(@PathVariable String id,
                                                     @RequestBody Map<String, Object> updates) {
        return new ResponseEntity<>(bookMapperImpl
                .toDTO(bookServiceImpl
                        .partialUpdateBook(id, updates)), HttpStatus.OK);
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookMapperImpl.toDTO(bookServiceImpl.getAllBooks());
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable String id) {
        BookEntity bookEntity = bookServiceImpl.getBookById(id);
        return bookMapperImpl.toDTO(bookEntity);
    }

    @GetMapping("/author/{id}")
    public List<BookDTO> getBooksByAuthor(@PathVariable UUID id) {
        return bookMapperImpl.toDTO(bookServiceImpl.getBooksByAuthor(id));
    }
}
