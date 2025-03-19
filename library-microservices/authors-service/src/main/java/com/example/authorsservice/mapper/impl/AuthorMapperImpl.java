package com.example.authorsservice.mapper.impl;

import com.example.authorsservice.domain.dto.AuthorDTO;
import com.example.authorsservice.domain.entity.AuthorEntity;
import com.example.authorsservice.mapper.Mapper;
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
        return modelMapper.map(entity, AuthorDTO.class);
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
