package com.epam.esm.dto;

public class TagDto {
    private long id;
    @Length(min = 2,max = 42)
    private String name;

    public TagDto(){}

    public TagDto(long id, String name) {
        this.id = id;
        this.name = name;
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
}
