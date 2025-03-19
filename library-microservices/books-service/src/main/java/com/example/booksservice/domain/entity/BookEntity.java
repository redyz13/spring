package com.example.booksservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import com.example.booksservice.annotation.ForeignKeyField;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class BookEntity {
    @Id
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ForeignKeyField
    private UUID authorId;
}
