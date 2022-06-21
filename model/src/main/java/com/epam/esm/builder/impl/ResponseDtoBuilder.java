package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ResponseDtoBuilder implements ModelBuilder {
    private int code;
    private String text;



    public ResponseDtoBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    public ResponseDtoBuilder setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public ResponseDto build() {
        return new ResponseDto(code,text);
    }
}
