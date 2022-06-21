package com.epam.esm.service;

import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    private static final int CODE_200 = 200;
    private static final int CODE_201 = 201;


    public ResponseDto okResponse(String text) {
        return createResponse(CODE_200,text);
    }

    public ResponseDto createdResponse(String text) {
        return createResponse(CODE_201,text);
    }

    private ResponseDto createResponse(int code,String text) {
        return new ResponseDtoBuilder()
                .setCode(code)
                .setText(text)
                .build();
    }
}
