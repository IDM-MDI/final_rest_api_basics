package com.epam.esm.task.builder;

public interface EntityBuilder {
    EntityBuilder setId(long id);

    EntityBuilder setName(String name);

    Object getResult();
}
