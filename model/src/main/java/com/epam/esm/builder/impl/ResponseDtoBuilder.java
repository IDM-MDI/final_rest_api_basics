package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.dto.ResponseDto;

public class ResponseDtoBuilder<T> implements ModelBuilder {
    private T content;
    private int code;
    private String text;


    public ResponseDtoBuilder<T> setContent(T content) {
        this.content = content;
        return this;
    }

    public ResponseDtoBuilder<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public ResponseDtoBuilder<T> setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public ResponseDto<T> build() {
        ResponseDto<T> result = new ResponseDto<>();
        result.setContent(content);
        result.setCode(code);
        result.setText(text);
        return null;
    }
}
