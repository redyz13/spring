package com.example.authorsservice.service;

import com.example.authorsservice.domain.entity.AuthorEntity;
import com.example.authorsservice.repository.AuthorRepository;
import com.example.authorsservice.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;

    @Test
    void testSaveAuthor_whenAuthorDoesNotExist() {
        UUID authorId = UUID.randomUUID();
        AuthorEntity author = AuthorEntity
                .builder()
                .id(authorId)
                .name("George Orwell")
                .age(46)
                .build();

        when(authorRepository.existsById(authorId)).thenReturn(false);
        when(authorRepository.save(author)).thenReturn(author);

        AuthorEntity savedAuthor = authorServiceImpl.saveAuthor(author);

        assertNotNull(savedAuthor);
        assertEquals(authorId, savedAuthor.getId());
        assertEquals("George Orwell", savedAuthor.getName());
        assertEquals(46, savedAuthor.getAge());

        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, times(1)).save(author);
    }
}
