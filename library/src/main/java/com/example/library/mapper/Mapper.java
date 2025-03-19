package com.example.library.mapper;

import java.util.List;

public interface Mapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);

    List<D> toDTO(List<E> entities);
    List<E> toEntity(List<D> dtos);
}
