package com.epam.esm.util.impl;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.entity.Role;
import com.epam.esm.util.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleModelMapper implements ModelMapper<Role, RoleDto> {
    @Override
    public Role toEntity(RoleDto dto) {
        return dto == null ? null : new Role(dto.getId(),dto.getName());
    }

    @Override
    public RoleDto toDto(Role entity) {
        return entity == null ? null : new RoleDto(entity.getId(),entity.getName());
    }

    @Override
    public List<Role> toEntityList(List<RoleDto> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<RoleDto> toDtoList(List<Role> entityList) {
        return entityList == null ? null : entityList.stream().map(this::toDto).toList();
    }
}
