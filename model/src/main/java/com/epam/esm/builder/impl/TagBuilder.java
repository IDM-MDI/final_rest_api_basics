package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagBuilder implements ModelBuilder {
    private Long id;
    private String name;
    private String status;

    public TagBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TagBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public Tag build() {
        Tag result = new Tag(id,name,status);
        clear();
        return result;
    }

    @Override
    public void clear() {
        id = null;
        name = null;
        status = null;
    }
}
