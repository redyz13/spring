package com.example.authservice.mapper.impl;

import com.example.authservice.domain.dto.UserDTO;
import com.example.authservice.domain.entity.UserEntity;
import com.example.authservice.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO toDTO(UserEntity bookEntity) {
        return modelMapper.map(bookEntity, UserDTO.class);
    }

    @Override
    public UserEntity toEntity(UserDTO bookDTO) {
        return modelMapper.map(bookDTO, UserEntity.class);
    }

    @Override
    public List<UserDTO> toDTO(List<UserEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> toEntity(List<UserDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity).collect(Collectors.toList());
    }
}
