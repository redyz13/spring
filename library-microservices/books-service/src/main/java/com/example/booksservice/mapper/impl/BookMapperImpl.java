package com.example.booksservice.mapper.impl;

import com.example.booksservice.domain.dto.BookDTO;
import com.example.booksservice.domain.entity.BookEntity;
import com.example.booksservice.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDTO toDTO(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    @Override
    public BookEntity toEntity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, BookEntity.class);
    }

    @Override
    public List<BookDTO> toDTO(List<BookEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookEntity> toEntity(List<BookDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity).collect(Collectors.toList());
    }
}
