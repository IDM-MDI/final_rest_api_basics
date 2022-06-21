package com.epam.esm.util.impl;

import com.epam.esm.dto.StatusDto;
import com.epam.esm.entity.Status;
import com.epam.esm.util.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusModelMapper implements ModelMapper<Status, StatusDto> {
    @Override
    public Status toEntity(StatusDto dto) {
        return dto == null ? null : new Status(dto.getId(),dto.getName());
    }

    @Override
    public StatusDto toDto(Status entity) {
        return entity == null ? null : new StatusDto(entity.getId(),entity.getName());
    }

    @Override
    public List<Status> toEntityList(List<StatusDto> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<StatusDto> toDtoList(List<Status> entityList) {
        return entityList == null ? null : entityList.stream().map(this::toDto).toList();
    }
}
