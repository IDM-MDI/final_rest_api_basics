package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagBuilder implements ModelBuilder {
    private Long id;
    private String name;
    private Status status;

    public TagBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Tag build() {
        Tag result = new Tag();
        result.setId(id);
        result.setName(name);
        result.setStatus(status);
        return result;
    }
}
