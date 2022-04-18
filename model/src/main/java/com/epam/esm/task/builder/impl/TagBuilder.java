package com.epam.esm.task.builder.impl;

import com.epam.esm.task.builder.EntityBuilder;
import com.epam.esm.task.entity.impl.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagBuilder implements EntityBuilder {
    private long id;
    private String name;


    @Override
    public TagBuilder setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public TagBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Tag getResult() {
        return new Tag(id,name);
    }
}
