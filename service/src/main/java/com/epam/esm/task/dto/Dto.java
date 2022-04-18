package com.epam.esm.task.dto;

import org.hibernate.validator.constraints.Length;
import java.util.Objects;

public abstract class Dto {
    protected long id;
    @Length(min = 2,max = 42)
    protected String name;

    public Dto(){}

    public Dto(long id,String name){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dto dto = (Dto) o;
        return id == dto.id && Objects.equals(name, dto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
