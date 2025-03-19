package com.example.library.mapper.impl;

import com.example.library.domain.dto.AuthorDTO;
import com.example.library.domain.entity.AuthorEntity;
import com.example.library.domain.entity.BookEntity;
import com.example.library.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, AuthorDTO> {
    private final ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDTO toDTO(AuthorEntity entity) {
        AuthorDTO authorDTO = modelMapper.map(entity, AuthorDTO.class);
        if (entity.getBookEntities() != null) {
            authorDTO.setWrittenBooks(entity.getBookEntities().stream()
                    .map(BookEntity::getIsbn)
                    .collect(Collectors.toList()));
        }
        return authorDTO;
    }

    @Override
    public AuthorEntity toEntity(AuthorDTO dto) {
        return modelMapper.map(dto, AuthorEntity.class);
    }

    @Override
    public List<AuthorDTO> toDTO(List<AuthorEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorEntity> toEntity(List<AuthorDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
