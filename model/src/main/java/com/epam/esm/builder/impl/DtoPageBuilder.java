package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;

import java.util.List;

public class DtoPageBuilder<T> implements ModelBuilder {
    private List<T> content;
    private ResponseDto response;
    private int size;
    private int numberOfPage;
    private String sortBy;
    private String direction;
    private ControllerType type;
    private boolean hasNext;
    private String param;

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

    public DtoPageBuilder<T> setResponse(ResponseDto response) {
        this.response = response;
        return this;
    }

    public DtoPageBuilder<T> setType(ControllerType type) {
        this.type = type;
        return this;
    }

    public DtoPageBuilder<T> setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public DtoPageBuilder<T> setParam(String param) {
        this.param = param;
        return this;
    }

    public DtoPageBuilder<T> setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public DtoPage<T> build() {
        DtoPage<T> result = new DtoPage<>();
        result.setContent(content);
        result.setResponse(response);
        result.setSize(size);
        result.setSortBy(sortBy);
        result.setNumberOfPage(numberOfPage);
        result.setType(type);
        result.setHasNext(hasNext);
        result.setParam(param);
        result.setDirection(direction);
        clear();
        return result;
    }

    @Override
    public void clear() {
        content = null;
        response = null;
        size = 0;
        sortBy = null;
        param = null;
        type = null;
        direction = null;
        hasNext = false;
        numberOfPage = 0;
    }
}
