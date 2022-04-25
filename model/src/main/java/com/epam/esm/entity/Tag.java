package com.epam.esm.entity;

public class Tag {
    private long id;
    private String name;
    private boolean isDeleted;

    public Tag(){}

    public Tag(String name) {
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "{\n" +
                "id = " + id +
                ",\n name = " + name +
                "}\n";
    }
}
