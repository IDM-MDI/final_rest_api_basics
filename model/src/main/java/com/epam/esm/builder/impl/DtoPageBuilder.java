package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.dto.DtoPage;

import java.util.List;

public class DtoPageBuilder<T> implements ModelBuilder {
    private List<T> content;
    private int size;
    private int numberOfPage;
    private String sortBy;

    public DtoPageBuilder<T> setContent(List<T> content) {
        this.content = content;
        return this;
    }

    public DtoPageBuilder<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public DtoPageBuilder<T> setNumberOfPage(int numberOfPage) {
        this.numberOfPage = numberOfPage;
        return this;
    }

    public DtoPageBuilder<T> setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public DtoPage<T> build() {
        DtoPage<T> result = new DtoPage<>();
        result.setContent(content);
        result.setSize(size);
        result.setSortBy(sortBy);
        result.setNumberOfPage(numberOfPage);
        return result;
    }
}
