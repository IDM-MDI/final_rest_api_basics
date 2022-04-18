package com.epam.esm.task.dto.impl;

import com.epam.esm.task.dto.Dto;
import com.epam.esm.task.entity.impl.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagDto extends Dto {

    public TagDto(){}

    public TagDto(long id, String name) {
        super(id, name);
    }

    public static TagDto toDto(Tag tag) {
        return new TagDto(tag.getId(),tag.getName());
    }
    public static Tag toEntity(TagDto tag) {
        return new Tag(tag.getId(),tag.getName());
    }

    public static List<TagDto> toDtoList(List<Tag> list) {
        List<TagDto> result = new ArrayList<>();
        list.forEach(i -> {
            result.add(toDto(i));
        });
        return result;
    }

    public static List<Tag> toEntityList(List<TagDto> list) {
        List<Tag> result = new ArrayList<>();
        list.forEach(i -> {
            result.add(toEntity(i));
        });
        return result;
    }

    @Override
    public String toString() {
        return "{\n" +
                " id = " + id +
                ",\n name = " + name +
                "}\n";
    }

}
