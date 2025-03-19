package com.example.library.controller;

import com.example.library.mapper.impl.BookMapperImpl;
import com.example.library.service.impl.BookServiceImpl;
import com.example.library.domain.dto.BookDTO;
import com.example.library.domain.entity.BookEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
