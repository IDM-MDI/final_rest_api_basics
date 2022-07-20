package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagModelMapper implements ModelMapper<Tag, TagDto> {

    private final TagBuilder builder;
    @Autowired
    public TagModelMapper(TagBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Tag toEntity(TagDto dto) {
        return dto == null ? null : builder.setId(dto.getId()).setName(dto.getName()).
                build();
    }

    @Override
    public TagDto toDto(Tag entity) {
        return entity == null ? null : new TagDto(entity.getId(),entity.getName());
    }

    @Override
    public List<Tag> toEntityList(List<TagDto> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<TagDto> toDtoList(List<Tag> entityList) {
        return entityList == null ? null : entityList.stream().map(this::toDto).toList();
    }
}
