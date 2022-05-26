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
        return builder.setId(dto.getId()).setName(dto.getName()).
                build();
    }

    @Override
    public TagDto toDto(Tag entity) {
        TagDto result = new TagDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        return result;
    }

    @Override
    public List<Tag> toEntityList(List<TagDto> dtoList) {
        List<Tag> result = new ArrayList<>();
        if(dtoList != null) {
            dtoList.forEach(i -> result.add(toEntity(i)));
        }
        return result;
    }

    @Override
    public List<TagDto> toDtoList(List<Tag> entityList) {
        List<TagDto> result = new ArrayList<>();
        if(entityList != null) {
            entityList.forEach(i -> result.add(toDto(i)));
        }
        return result;
    }
}
