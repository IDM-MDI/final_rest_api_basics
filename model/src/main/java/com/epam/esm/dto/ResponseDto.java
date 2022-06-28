package com.epam.esm.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ResponseDto {
    private int code;
    private String text;
}
