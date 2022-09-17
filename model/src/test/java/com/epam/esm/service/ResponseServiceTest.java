package com.epam.esm.service;

import com.epam.esm.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseServiceTest {
    private static final ResponseService service = new ResponseService();

    @Test
    void okResponse() {
        ResponseDto actual = service.okResponse("testResponse");
        ResponseDto expected = new ResponseDto(200,"testResponse");
        Assertions.assertEquals(actual,expected);
    }

    @Test
    void createdResponse() {
        ResponseDto actual = service.createdResponse("testResponse");
        ResponseDto expected = new ResponseDto(201,"testResponse");
        Assertions.assertEquals(actual,expected);
    }
}