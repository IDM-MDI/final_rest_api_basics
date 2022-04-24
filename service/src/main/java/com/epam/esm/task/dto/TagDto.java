package com.epam.esm.task.dto;

import com.epam.esm.task.entity.impl.Tag;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagDto {

    private long id;
    @Length(min = 2,max = 42)
    private String name;

    public TagDto(){}

    public TagDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagDto toDto(Tag tag) {
        return new TagDto(tag.getId(),tag.getName());
    }
    public static Tag toEntity(TagDto tag) {
        return new Tag(tag.getId(),tag.getName());
    }

    public static List<TagDto> toDtoList(List<Tag> list) {
        List<TagDto> result = new ArrayList<>();
        if(list != null) {
            list.forEach(i -> {
                result.add(toDto(i));
            });
        }
        return result;
    }

    public static List<Tag> toEntityList(List<TagDto> list) {
        List<Tag> result = new ArrayList<>();
        if(list != null) {
            list.forEach(i -> {
                result.add(toEntity(i));
            });
        }
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "{\n" +
                " id = " + id +
                ",\n name = " + name +
                "}\n";
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return id == tagDto.id && Objects.equals(name, tagDto.name);
    }

    @Override public int hashCode() {
        return Objects.hash(id, name);
    }
}
