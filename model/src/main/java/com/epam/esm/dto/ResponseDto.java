package com.epam.esm.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ResponseDto<T> {
    private T content;
    private int code;
    private String text;
}
